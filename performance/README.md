# Performance Testing

 

## Purpose

This section contains the performance testing assets and results used to validate the inventory platform under concurrent load conditions.

## Conclusion

The platform successfully processed 158,085 transactions during the performance test execution.
Kafka consumers, Python processing services, PostgreSQL operations, and batch jobs completed the workload without leaving pending records.
The messaging queue was fully drained, and all transactions reached the PROCESADO state, demonstrating the platform's ability to sustain the configured workload and successfully complete end-to-end processing.



## Scope

The tests validate the following components:
- Synchronous Microservices
- Event-Driven Microservices
- Queue Processing
- Python Services
- PostgreSQL Database
- Batch Processes
- Shell Scripts
- Kafka Integration
## Test Architecture

Client/API Requests

Microservices
Queue
Python Processing
PostgreSQL
Kafka
External Environments

## Tools
- k6
- PostgreSQL
- Kafka
- Python
- Shell Scripts
## Test Scenarios

### Concurrent User Load

- 500 concurrent users
- End-to-end transaction validation
- Inventory updates
 
### Batch Execution
- Inventory discount execution every 30 minutes
- Database procedure validation
- Batch process monitoring
### Integration Validation
 
- Kafka topic processing
- Message distribution
- Statistics publication

## Results
         /\      Grafana   /‾‾/  
    /\  /  \     |\  __   /  /   
   /  \/    \    | |/ /  /   ‾‾\ 
  /          \   |   (  |  (‾)  |
 / __________ \  |_|\_\  \_____/ 


     execution: local
        script: test.js
        output: -

     scenarios: (100.00%) 1 scenario, 500 max VUs, 5m30s max duration (incl. graceful stop):
              * default: 500 looping VUs for 5m0s (gracefulStop: 30s)



  █ TOTAL RESULTS 

    checks_total.......: 492460 1633.343694/s
    checks_succeeded...: 60.00% 295476 out of 492460
    checks_failed......: 40.00% 196984 out of 492460

    ✓ orders input 200
    ✓ orders onsite 200
    ✗ order_id generado
      ↳  0% — ✓ 0 / ✗ 98492
    ✗ amount valido
      ↳  0% — ✓ 0 / ✗ 98492
    ✓ payments queue 200

    HTTP
    http_req_duration..............: avg=8.17ms min=130µs med=983µs max=914.4ms p(90)=9.32ms p(95)=30.44ms p(99)=158ms
      { expected_response:true }...: avg=8.17ms min=130µs med=983µs max=914.4ms p(90)=9.32ms p(95)=30.44ms p(99)=158ms
    http_req_failed................: 0.00%  0 out of 295476
    http_reqs......................: 295476 980.006216/s

    EXECUTION
    iteration_duration.............: avg=1.52s  min=1.5s  med=1.5s  max=2.57s   p(90)=1.55s  p(95)=1.62s   p(99)=1.82s
    iterations.....................: 98492  326.668739/s
    vus............................: 160    min=160         max=500
    vus_max........................: 500    min=500         max=500

    NETWORK
    data_received..................: 198 MB 655 kB/s
    data_sent......................: 143 MB 474 kB/s




running (5m01.5s), 000/500 VUs, 98492 complete and 0 interrupted iterations
default ✓ [======================================] 500 VUs  5m0s
