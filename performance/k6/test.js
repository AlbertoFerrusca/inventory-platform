import http from 'k6/http';
import { sleep,check } from 'k6';
import { Counter } from 'k6/metrics';
import exec from 'k6/execution';

export const options = {
  vus: 500,           // usuarios concurrentes
  duration: '5m', 
    
  summaryTrendStats: [
      'avg',
      'min',
      'med',
      'max',
      'p(90)',
      'p(95)',
      'p(99)'
    ]  // duración
};

function fakeUuid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
  .replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
  return v.toString(16);
  });
}
export default function () {

  const payloadLina = JSON.stringify({
    employId: "TestSanti",
    cliente: "Cliente4",
    estatus: "DRAFTED",
    totalCompra: 9757.29,
    external_id: Math.random().toString(),
    items: [
      { productoId: "P001", cantidad: 15, precio: 100.5},
      { productoId: "P002", cantidad: 10, precio: 200},
      { productoId: "P003", cantidad: 12, precio: 150.75},
      { productoId: "P004", cantidad: 8, precio: 80},
      { productoId: "P005", cantidad: 9, precio: 300.1},
      { productoId: "P006", cantidad: 11, precio: 99.99 }
     ]
  });

  const paymentqueue = JSON.stringify({
    employId: "TestLeo",
    cliente: "Cliente5",
    estatus: "DRAFTED",
    totalCompra: 21411.4,
    external_id: Math.random().toString(),
    items: [
      { productoId: "P010", cantidad: 21, precio: 120.4 },
      { productoId: "P011", cantidad: 14, precio: 130 },
      { productoId: "P012", cantidad: 22, precio: 78.5 },
      { productoId: "P014", cantidad: 100, precio: 90 },
      { productoId: "P015", cantidad: 45, precio: 140.8 }
    ]
  });
  
  const payloadPaymentAsync=JSON.stringify({
  "order_id": fakeUuid(),
  "amount": 511.5, 
  "currency": "MXN", //se puede ignoarar
  "status": "PENDING",
  "payment_method": "CARD",
  "installments": 12,
  "branch_id": "branch-A",  //se puede ignoarar
  "estado": "Jalisco",
  "bank_folio": "ABC123456"


  });
   const payloadPaymentque=JSON.stringify({
   "order_id": fakeUuid(),
   "amount": 511.5,
   "currency": "MXN", //se puede ignoarar
   "status": "PENDING",
   "payment_method": "CARD",
  "installments": 12,
  "branch_id": "branch-A",  //se puede ignoarar
  "estado": "Jalisco",
  "bank_folio": "ABC123456"

   });
  const params = {
    headers: { 'Content-Type': 'application/json' }
  };


  let rest =http.post('http://localhost:5050/App/Orders/input', paymentqueue, params);
  check(rest, {'orders input 200': (r) => r.status >= 200 && r.status < 300,});
  sleep(0.5); 
  rest =http.post('http://localhost:5050/App/Orders/onsite',payloadLina, params); 
  check(rest, {'orders onsite 200': (r) => r.status >= 200 && r.status < 300,});
  sleep(0.5);
  let responseJson = JSON.parse(rest.body);
  //const realData = JSON.stringify(responseJson); 
  check(responseJson, {'order_id generado': (r) => !!r.order_id,'amount valido': (r) => r.amount > 0});
  
  const RealPayment= JSON.stringify({
  "order_id":responseJson.orderId,
  "amount":responseJson.totalCompra, 
  "currency": "MXN", //se puede ignoarar
  "status": "PENDING",
  "payment_method": "CARD",
  "installments": 12,
  "branch_id": "branch-A",  //se puede ignoarar
  "estado": "Jalisco",
  "bank_folio": "ABC123456"
});

   const useRealOnAsync = exec.scenario.iterationInTest % 2 === 0;
   const asyncPayload = useRealOnAsync ? RealPayment : payloadPaymentAsync;
   const queuePayload = useRealOnAsync ? payloadPaymentAsync : RealPayment;

  rest =http.post('http://localhost:5051/api/v1/payments/async',asyncPayload,params)
  check(rest, {'payments async 200': (r) => r.status >= 200 && r.status < 300,});
  sleep(0.5);  
  rest =http.post('http://localhost:5051/api/v1/payments/queue',RealPayment,params)
  check(rest, {'payments queue 200': (r) => r.status >= 200 && r.status < 300,});
  sleep(0.5);
}

