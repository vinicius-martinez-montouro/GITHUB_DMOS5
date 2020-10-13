package br.edu.dmos5.github_dmos5.service;

import java.util.List;

import br.edu.dmos5.github_dmos5.model.Repository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Service to search to repository github
 * @author vinicius.montouro
 */
public interface RetrofitService {

    @GET("{path}")
    Call<List<Repository>> findByName(@Path(value = "path", encoded = true) String userName);

}
