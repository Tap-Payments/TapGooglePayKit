package company.tap.google.pay.internal.tapnetworkkit.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import kotlin.ParameterName;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIRequestInterface {

    @POST()
    Call<JsonElement> postRequest(@Url String apiName, @Body Map map);

    @GET()
    Call<JsonElement> getRequest(@Url String apiName);

    @PUT()
    Call<JsonElement> putRequest(@Url String apiName);

    @DELETE()
    Call<JsonElement> delete(@Url String apiName);

}
