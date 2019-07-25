import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={
        InvoiceRestTest.class,
        InvoiceTest.class,
        ItemTest.class,
})

public class TestSuite {
}
