# Database DDL
2
 
3
## Purpose
4
 
5
This directory contains all PostgreSQL Data Definition Language (DDL) scripts used to create and maintain the database schema of the inventory platform.
6
 
7
## Contents
8
 
9
DDL scripts may include:

- Tables

- Primary Keys
  within definition
- Foreign Keys
  within definition
- Indexes
  within definition 
- Constraints
  within definition
- Sequences
   NA
- Partition Definitions
   NA at this moment
## Organization
Each database object should be maintained in a separate file whenever possible.

Example:
ddl/
├── products.sql
├── inventory.sql
├── orders.sql
├── payments.sql
└── branches.sql
