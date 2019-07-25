#This script autopopulates dummy invoice data to a local MongoDB instance
#Requred - pip install pymongo
#Just type "python3 MongoUtil.py invoice create"
#Does not work if there is any data in the collection, so you may need to run
#"python3 MongoUtil.py invoice delete" command if your having an issue

import pymongo
import sys
from datetime import datetime

myclient = pymongo.MongoClient("mongodb://localhost:27017")

#Database name
mydb = myclient["invoiceTest"]
#Collection name
invoices = mydb["invoice"]

def InvoiceCreateDatabase():
    mylist = [
	{
	"_id":"1",
	"version":0,
	"lookupID":133,
	"customerID":105,
	"date": datetime.now().strftime("%Y"+"-"+"%m"+"-"+"%d"),
	"totalCost":404,
	"totalSale":0,
	"items":[]
	},
	{
	"_id": "2",
    "version":0,
	"lookupID": 777,
	"customerID": 105,
	"date": datetime.now().strftime("%Y"+"-"+"%m"+"-"+"%d"),
	"totalCost": 555,
	"totalSale": 200,
	"items": [
		{
		"itemLookupID": 102,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 10
		},
		{
		"itemLookupID": 400,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 10
		}
		]
	},
	{
	"_id": "3",
    "version":0,
	"lookupID": 302,
	"customerID": 106,
	"date": datetime.now().strftime("%Y"+"-"+"%m"+"-"+"%d"),
	"totalCost": 30,
	"totalSale": 400,
	"items": [
		{
		"itemLookupID": 311,
		"quantity": 20,
		"cost": 6,
		"sellPrice": 20
		}
		]
	},
	{
	"_id": "4",
    "version":0,
	"lookupID": 100,
	"customerID": 107,
	"date": datetime.now().strftime("%Y"+"-"+"%m"+"-"+"%d"),
	"totalCost": 555,
	"totalSale": 1000,
	"items": [
		{
		"itemLookupID": 6,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 100
		},
		{
		"itemLookupID": 14,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 0
		},
		{
		"itemLookupID": 15,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 0
		}
		]
	},
	{
	"_id": "5",
    "version":0,
	"lookupID": 101,
	"customerID": 108,
	"date": datetime.now().strftime("%Y"+"-"+"%m"+"-"+"%d"),
	"totalCost": 60,
	"totalSale": 1000,
	"items": [
		{
		"itemLookupID": 6,
		"quantity": 10,
		"cost": 5,
		"sellPrice": 100
		}
		]
	}
	]
    invoices.insert_many(mylist)
    print("Populated Invoice Collection.")
def InvoiceDeleteCollection():
    invoices.drop()
    print("Dropped Invoice Collection")




if(len(sys.argv) > 2):
    if(sys.argv[1] == "invoice"):
        if(sys.argv[2] == "create"):
            InvoiceCreateDatabase()
        elif(sys.argv[2] == "delete"):
            InvoiceDeleteCollection()
    else:
        print("Invalid. HELP: python3 MongoUtil.py [OPTION]")
        print("[OPTION]: create, delete, help")
else:
    print("Invalid. HELP: python3 MongoUtil.py [OPTION]")
    print("[OPTION]: create, delete, help")

