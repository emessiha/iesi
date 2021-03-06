#!/bin/bash
#UNIX LINEFEEDS!

# Immediately exits if any error occurs during the script
# execution. If not set, an error could occur and the
# script would continue its execution.
set -o errexit


# Creating an array that defines the environment variables
# that must be set. This can be consumed later via arrray
# variable expansion ${REQUIRED_ENV_VARS[@]}.
readonly REQUIRED_ENV_VARS=(
  "FOTG_DB_USER"
  "FOTG_DB_PASSWORD"
  "FOTG_DB_DATABASE"
  "POSTGRES_USER")


# Main execution:
# - verifies if all environment variables are set
# - runs the SQL code to create user and database
main() {
  check_env_vars_set
  init_user_and_db
  init_schema
}


# Checks if all of the required environment
# variables are set. If one of them isn't,
# echoes a text explaining which one isn't
# and the name of the ones that need to be
check_env_vars_set() {
  for required_env_var in ${REQUIRED_ENV_VARS[@]}; do
    if [[ -z "${!required_env_var}" ]]; then
      echo "Error:
    Environment variable '$required_env_var' not set.
    Make sure you have the following environment variables set:
      ${REQUIRED_ENV_VARS[@]}
Aborting."
      exit 1
    fi
  done
}


# Performs the initialization in the already-started PostgreSQL
# using the preconfigured POSTGRE_USER user.
init_user_and_db() {
  echo "Creating database FOTG"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
     CREATE USER $FOTG_DB_USER WITH PASSWORD '$FOTG_DB_PASSWORD';
     CREATE DATABASE $FOTG_DB_DATABASE;
     GRANT ALL PRIVILEGES ON DATABASE $FOTG_DB_DATABASE TO $FOTG_DB_USER;
EOSQL
}

# Create the initial tables
init_schema() {
  echo "Initialising schema"
  psql -v ON_ERROR_STOP=1 --username "$FOTG_DB_USER" <<-EOSQL
	CREATE TABLE exports (
		export_date date PRIMARY KEY,
		orders_file	VARCHAR(50)    NOT NULL,
		products_file	VARCHAR(50)    NOT NULL
	);
	INSERT INTO exports(export_date, orders_file, products_file) VALUES ('2019-01-01', 'orders1234.csv', 'products5642.csv');
	CREATE TABLE CUSTOMERS
	(
		ID				NUMERIC NOT NULL,
		DateJoined		TIMESTAMP,
		BirthDate		TIMESTAMP,
		FirstName		TEXT,
		LastName			TEXT,
		Phone				TEXT,
		Street				TEXT,
		City				TEXT,
		Country				TEXT,
		Gender				TEXT,
		Email				TEXT
	);
	CREATE TABLE RESTAURANTS
	(
		ID				NUMERIC NOT NULL,
		DateJoined		TIMESTAMP,
		Name				TEXT,
		Street				TEXT,
		City				TEXT,
		Country				TEXT,
		Email				TEXT
	);
	CREATE TABLE ORDERS
	(
		ID				NUMERIC NOT NULL,
		CustomerID		NUMERIC NOT NULL,
		RestaurantID		NUMERIC NOT NULL,
		OrderDate		TIMESTAMP,
		Price				NUMERIC
	);
EOSQL
}


# Executes the main routine with environment variables
# passed through the command line. We don't use them in
# this script but now you know ??
main "$@"