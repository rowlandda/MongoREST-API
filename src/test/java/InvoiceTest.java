import com.david.mongorest.models.Invoice;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {
    Invoice invoice;
    String tempID;
    JSONObject jsonInvoice;


    @Before
    public void createNewInvoiceWithValidJSON() throws JSONException {
        this.invoice = new Invoice();
        this.tempID = this.invoice.get_id();
        this.jsonInvoice = this.invoice.createJSONObject();
    }

    @Test
    public void createJSONObject() {
        JSONObject jsonTest = this.invoice.createJSONObject();
        assertEquals(jsonInvoice.toString(), jsonTest.toString());
    }

    @Test
    public void loadFromJSON() {
        Invoice testInvoice = invoice.loadFromJSON(jsonInvoice);
        assertEquals(invoice, testInvoice);
    }

    @Test
    public void isEqual() {
        Invoice b = new Invoice(this.invoice);

        boolean isSame = this.invoice.equals(b);
        assert(isSame);

    }

    @Test
    public void isEqualFalse(){
        Invoice b = new Invoice();

        boolean isSame = this.invoice.equals(b);
        assert(!isSame);
    }
}