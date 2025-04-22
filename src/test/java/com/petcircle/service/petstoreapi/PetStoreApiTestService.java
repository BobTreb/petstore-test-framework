package com.petcircle.service.petstoreapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcircle.model.petstoreapi.Pet;
import com.petcircle.model.common.RequestArtifact;
import com.petcircle.utils.ApiUtils.ApiUtil;
import com.petcircle.utils.TestUtils.TestResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class PetStoreApiTestService {

    private final ApiUtil apiUtil;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    @Autowired
    public PetStoreApiTestService(ApiUtil apiUtil, String baseUrl) {
        this.apiUtil = apiUtil;
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    public RequestArtifact createNewPetRequestArtifact() throws IOException {
        return buildRequestArtifact("data/petstoreapi/newPet.json");
    }

    public RequestArtifact updatePetRequestArtifact() throws IOException {
        return buildRequestArtifact("data/petstoreapi/updatePet.json");
    }

    public RequestArtifact petByIdRequestArtifact(Integer id) {
        String url = baseUrl + "/pet/" + id;
        Map<String, String> headers = apiUtil.headerBuilder();
        return new RequestArtifact(url, headers, null, null);
    }

    private RequestArtifact buildRequestArtifact(String resourcePath) throws IOException {
        try (InputStream resourceStream = TestResourceUtil.getResourceStream(resourcePath)) {
            Pet petRequestBody = objectMapper.readValue(resourceStream, Pet.class);
            String url = baseUrl + "/pet";
            Map<String, String> headers = apiUtil.headerBuilder();
            return new RequestArtifact(url, headers, petRequestBody, null);
        }
    }
}
