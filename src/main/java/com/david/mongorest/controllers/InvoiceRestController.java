package com.david.mongorest.controllers;

import com.david.mongorest.models.Invoice;
import com.david.mongorest.models.Item;

import com.david.mongorest.models.ItemSummary;
import com.david.mongorest.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Validated
public class InvoiceRestController {
	@Autowired
    private InvoiceRepository repository;

	//return list of all invoices
	@RequestMapping(value = "/invoice/", method = RequestMethod.GET)
	public ResponseEntity<?> getAllInvoices()
	{
		List<Invoice> inv = repository.findAll();
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(inv,HttpStatus.OK);
		}
	}

	//get specific invoice by _id
	@RequestMapping(value = "/invoice/{_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInvoiceById(@PathVariable("_id") String _id) {
		Invoice inv = repository.findBy_id(_id);
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(inv,HttpStatus.OK);
		}
	}
	
	//create new invoice
	@RequestMapping(value = "/invoice/", method = RequestMethod.POST)
	public ResponseEntity<?> saveInvoice(@Valid @RequestBody Invoice saveInv) {
		int totalSale = 0;
		saveInv.setTotalSale(0);
		for (Item item : saveInv.getItems()) {
			totalSale += (item.getSellPrice() * item.getQuantity());
		}
		saveInv.setTotalSale(totalSale);
		
		return new ResponseEntity<>(repository.save(saveInv), HttpStatus.CREATED);
	}

	//modify invoice
	@RequestMapping(value = "/invoice/{_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> modifyInvoiceByID(@PathVariable("_id") String _id, @RequestBody Invoice saveInv) {
		Invoice orgInv = repository.findBy_id(_id);
		if(orgInv == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if(!orgInv.get_id().equals(saveInv.get_id())){
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		//concurrencty problem check
		if(orgInv.getVersion() != saveInv.getVersion()) {
			return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
							.body("Another user has modified the data");
		}
		int totalSale = 0;
		saveInv.setTotalSale(0);
		for (Item item : saveInv.getItems()) {
			totalSale += (item.getSellPrice() * item.getQuantity());
		}
		saveInv.setTotalSale(totalSale);
		saveInv.set_id(_id);
		saveInv.incrementVersion();
		
		return new ResponseEntity<>(repository.save(saveInv), HttpStatus.OK);
	}

	//delete invoice
	@RequestMapping(value = "/invoice/{_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInvoice(@PathVariable("_id") String _id) {
		repository.delete(repository.findBy_id(_id));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}




	//search by customer ID
	@RequestMapping(value = "/invoice/searchByCustomerID", method = RequestMethod.GET)
	public ResponseEntity<?> searchByCustomerID(@RequestParam(name = "customerID") int customerID) {
		List<Invoice> inv = repository.findByCustomerID(customerID);
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(inv,HttpStatus.OK);
		}
	}

	//return list of invoices with sales less than target number
	@RequestMapping(value = "/invoice/totalSaleLessThan", method = RequestMethod.GET)
	public ResponseEntity<?> getSalesLessThan(@RequestParam(name = "totalSale") int totalSale) {
		List<Invoice> inv = repository.findByTotalSaleLessThan(totalSale);
		
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(inv,HttpStatus.OK);
		}
		
	}

	//return list of invoices with sales greater than target number
	@RequestMapping(value = "/invoice/totalSaleGreaterThan", method = RequestMethod.GET)
	public ResponseEntity<?> getSalesGreaterThan(@RequestParam(name = "totalSale") int totalSale) {
		
		List<Invoice> inv = repository.findByTotalSaleGreaterThan(totalSale);
		
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(inv,HttpStatus.OK);
		}
	}

	//return summary of items sold
	@RequestMapping(value = "/invoice/getSaleSummary", method = RequestMethod.GET)
	public ResponseEntity<?> getItemSummary()
	{
		List<Invoice> inv = repository.findAll();
		if(inv == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			List<Item> itemList;
			HashMap<Integer, Integer> summary = new HashMap<>();
			for (Invoice invoice : inv) {
				itemList = Arrays.asList(invoice.getItems());
				for (Item item : itemList) {
					if (summary.containsKey(item.getItemLookupID())) {
						int additionalQuantity = item.getQuantity();
						int previousQuantity = summary.get(item.getItemLookupID());
						int total = additionalQuantity + previousQuantity;
						summary.put(item.getItemLookupID(), total);
					}else {
						summary.put( item.getItemLookupID(), item.getQuantity());
					}
				}
			}
			ItemSummary itemSummary = new ItemSummary(summary);
			return new ResponseEntity<>(itemSummary,HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/invoice/dateBetween", method = RequestMethod.GET)
	public ResponseEntity<?> getInvoicesBetween(@RequestParam(name = "dateStart") String dateStart,
												@RequestParam(name = "dateEnd") String dateEnd) {

		if (dateStart.length() < 10 || dateEnd.length() < 10) {
			return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
						.body("invalid date format");
		}

	    List<Invoice> invoices = repository.findAllByDateBetween(dateStart, dateEnd);
		LocalDate startDateTime = LocalDate.parse(dateStart).plusDays(1);
		LocalDate endDateTime = LocalDate.parse(dateEnd);
		HashMap<String, Integer> summary = new HashMap<>();
		int quantity = 0;
		while (startDateTime.isBefore(endDateTime)) {
		   for (Invoice invoice : invoices) {
		   	if (LocalDate.parse(invoice.getDate()).isEqual(startDateTime)) {
		   		quantity++;
			}
		   }
		   summary.put(startDateTime.toString(), quantity);
		   quantity = 0;
		   startDateTime = startDateTime.plusDays(1);
		}
        return new ResponseEntity<>(summary,HttpStatus.OK);
	}
}