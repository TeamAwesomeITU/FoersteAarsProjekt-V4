package inputHandler.test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddressParserExistingAddressTester.class, AddressParserExpectedExceptionTester.class})
public class AddressParserAllTests {

}
