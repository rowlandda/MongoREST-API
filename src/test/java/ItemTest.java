import com.david.mongorest.models.Item;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    Item item;
    JSONObject jsonItem;

    @Before
    public void createNewItemWithValidJSON() throws JSONException {
        this.item = new Item();
        this.jsonItem = this.item.createJSONObject();
    }

    @Test
    public void createJSONObject() {
        JSONObject jsonTest = this.item.createJSONObject();
        assertEquals(jsonItem.toString(), jsonTest.toString());
    }

    @Test
    public void loadFromJSON() {
        Item testItem = item.loadFromJSON(jsonItem);
        assertEquals(item, testItem);
    }

    @Test
    public void isEqual() {
        Item b = new Item(this.item);

        boolean isSame = this.item.equals(b);
        assert(isSame);

    }

    @Test
    public void isEqualFalse(){
        Item b = new Item();
        //item does not have a unique identifier so we must change one field
        //for this test when we did not for the invoice testing
        b.setSellPrice(Integer.MAX_VALUE);

        boolean isSame = this.item.equals(b);
        assert(!isSame);
    }
}
