import com.pingan.haofang.network.response.HttpResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EnvService {

    @GET("public/frontend/version")
    Call<HttpResponseBody<H5VersionResponse>> getH5Version();

}
