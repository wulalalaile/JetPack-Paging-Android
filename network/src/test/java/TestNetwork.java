import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.server.H5ServerRetrofit;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestNetwork extends TestCase {

    @BeforeClass
    public void before() {
    }

    @Test
    public void testH5VersionRequest() throws IOException {
        Map<String, String> commonParameterMap = new HashMap<>();
        commonParameterMap.put("_from", "android");
        commonParameterMap.put("app_id", "7");

        H5ServerRetrofit.init("http://api.an2.anhouse.cn/".replace("api", "hft.m"));
        H5ServerRetrofit.setCommonParameterMap(commonParameterMap);
        HttpResponseBody<H5VersionResponse> body = H5ServerRetrofit.getInstance()
                .create(EnvService.class)
                .getH5Version().execute().body();
        assertEquals("MyResponse{code=0, msg='ok', data=H5VersionResponse{enable=0, force=0, txt='null', key='null', url='null'}}", body.toString());
    }
}
