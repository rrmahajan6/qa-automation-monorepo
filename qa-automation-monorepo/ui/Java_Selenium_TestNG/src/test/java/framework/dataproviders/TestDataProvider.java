package framework.dataproviders;

import com.google.gson.JsonArray;
import framework.utils.TestDataUtil;
import org.testng.annotations.DataProvider;

public class TestDataProvider {

    @DataProvider(name = "parallelLoginData", parallel = true)
    public static Object[][] parallelLoginData() {
        JsonArray dataArray = TestDataUtil.loadJsonArrayFromClasspath("testdata/login-data.json");
        return TestDataUtil.jsonArrayToDataProvider(dataArray);
    }

    @DataProvider(name = "statefulPurchaseData", parallel = false)
    public static Object[][] statefulPurchaseData() {
        JsonArray dataArray = TestDataUtil.loadJsonArrayFromClasspath("testdata/purchase-data.json");
        return TestDataUtil.jsonArrayToDataProvider(dataArray);
    }
}
