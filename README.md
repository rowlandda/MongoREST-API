## mongorest

RESTful API that interacts with a MongoDB database containing an arbitrary invoice document structure. Implemented in Java with Spring Boot and Maven.  For dev mode `application.properties` needs to have the line `spring.profiles.active=dev`, and the server looks for a MongoDB named `invoiceTest` on `localhost:27017` without login credentials required (MongoDB defaults).  The collection of invoice documents inside `invoiceTest` must be named `invoice`.

# Example Invoice JSON structure
```
{
  "_id" : "4",
  "version" : 0,
  "lookupID" : 100,
  "customerID" : 107,
  "date" : "2019-07-11",
  "totalCost" : 555,
  "totalSale" : 1000,
  "items" : [
    {
      "itemLookupID" : 6,
      "quantity" : 10,
      "cost" : 5,
      "sellPrice" : 100
    },
    {
      "itemLookupID" : 14,
      "quantity" : 10,
      "cost" : 5,
      "sellPrice" : 0
    },
    {
      "itemLookupID" : 15,
      "quantity" : 10,
      "cost" : 5,
      "sellPrice" : 0
    }
  ]
}
```
  
 # HTTP requests
 
 ## List All Invoices
`curl -i -s -X -k GET http://localhost:8080/invoice/`
 ## Get Invoice By MongoDB _id
`curl -i -s -X -k GET http://localhost:8080/invoice/{id}` 
 ## Create Invoice
 `curl -i -s -H -k "Content-Type: application/json" -X POST -d '{Invoice JSON payload}' http://localhost:8080/invoice/`
 ## Modify Invoice By MongoDB _id
 `curl -i -s -H -k "Content-Type: application/json" -X PUT -d '{Invoice JSON payload}' http://localhost:8080/invoice/{_id}`
 ## Delete Invoice
 `curl -i -s -X -k DELETE http://localhost:8080/invoice/{_id}`
 ## Search by customer-id
 `curl -i -s -X -k GET http://localhost:8080/invoice/searchByCustomerID?customerID={desired customer-id}`
 ## Get invoices by total sale price (less than target)
 `curl -i -s -X -k GET http://localhost:8080/invoice/totalSaleLessThan?totalSale={sale target}`
 ## Get invoices by total sale price (greater than target)
 `curl -i -s -X -k GET http://localhost:8080/invoice/totalSaleGreaterThan?totalSale={sale target}`
 ## Get a summary of the amount of each item sold
  `curl -i -s -X -k GET http://localhost:8080/invoice/getSaleSummary`
 ## Get number of invoices created on each day between two dates* 
`curl -i -s -X -k GET http://localhost:8080/invoice/dateBetween?dateStart={start date}&dateEnd{end date}`

*(dates must be in 'YYYY-MM-DD' format and are not inclusive)
