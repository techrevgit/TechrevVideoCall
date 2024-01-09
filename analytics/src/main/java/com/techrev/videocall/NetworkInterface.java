package com.techrev.videocall;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface NetworkInterface
{
    @Headers("Content-Type: application/json")
    @POST("getarequest")
    Call<MeetingDetailsModel> getAllMeetingDetails(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getRequestparticipant")
    Call<ParticipantDetailsModel> getAllParticipantDetails(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getarequest")
    Call<ResponseBody> getRequestDetails(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getDocumentsByRequestId")
    Call<DocumentsByRequestIdModel> getDocumentsByRequestID(@Header("Authorization") String token, @Body String body);


    @Headers("Content-Type: application/json")
    @POST("getRequestForMeetingRoom")
    Call<RequestForMeetingRoomModel> getRequestForMeetingRoom(@Header("Authorization") String token, @Body String body);


    @Headers("Content-Type: application/json")
    @POST("getClientActivePlanDetailsByClientId")
    Call<ClientActivePlanDetailsByClientIdModel> getClientActivePlanDetailsByClientId(@Header("Authorization") String token, @Body String body);

    // Newly Added for Multipart for upload files
    @Multipart
    @POST("insertRequestImages")
    Call<UploadImageModel> uploadFile(@Header("Authorization") String token,
                                      @Part MultipartBody.Part file,
                                      @Part("file") RequestBody name,
                                      @Part("documentName") String docName,
                                      @Part("isDewDoc") Boolean isDewDocName,
                                      @Part("requestId") String requestId);

    @Multipart
    @POST("uploadAttachmentFile")
    Call<AttachedFileUploadResponseModel> uploadAttachedFile(@Header("Authorization") String token,
                                                                                   @Part MultipartBody.Part file,
                                                                                   @Part("file") RequestBody name,
                                                                                   @Part("documentName") String docName,
                                                                                   @Part("isDewDoc") Boolean isDewDocName,
                                                                                   @Part("uploadedBy") String requestId);


    @Headers("Content-Type: application/json")
    @POST("updateDocumentName")
    Call<ResponseBody> updateDocumentName(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("deleteFileName")
    Call<ResponseBody> deleteFileName(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("downloadFile")
    @Streaming
    Call<ResponseBody> downloadImage(@Header("Authorization") String token,@Query("DocId") String docID);



    @Headers("Content-Type: application/json")
    @POST("updateRequestParticipant")
    Call<ResponseBody> locationData(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("insertCustomerSharedLocationMobile")
    Call<ResponseBody> shareLocationData(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getCustomer")
    Call<SearchUserResponse> searchUser(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("addCosignerDuringMeeting")
    Call<ResponseBody> addCosignerDuringMeeting(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getRequestCosignerByRequestIdAndUserId")
    Call<SearchUserResponse> checkCosignerExist(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getAllMessagesByMeetingId")
    Call<ChatDataModel> getAllMessagesByMeetingId(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("insertMessageDetails")
    Call<ChatDataModel> saveNewMessage(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("deleteMyMessage")
    Call<DeleteMessageResponseModel> deleteMessage(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getRequestCosignerByRequestId")
    Call<CosignerDetailsModel> getAllCoSignerDetailsByMeetingId(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getCustomerDisagreeCount")
    Call<CustomerDisagreeCountModel> getCustomerDisagreeCount(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("updateDisagreeCount")
    Call<CustomerDisagreeCountModel> updateDisagreeCount(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("updateIsCustomerAgreedDetails")
    Call<CustomerDisagreeCountModel> updateAgreeCount(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getUserAccessCodeAndIDMSessionIdByUserId")
    Call<CosignerVerificationModel> checkIfKBADoneOfUserByID(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getRequestCosignerByRequestId")
    Call<CosignerDetailsModel> getCosignerDetailsByRequestId(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("updateRequestStatusByCustomer")
        Call<UpdateRequestStatusResponse> updateRequestStatusByCustomer(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("notifyServiceProvider")
    Call<UpdateRequestStatusResponse> notifyServiceProvider(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getRequestDocumentsById")
    Call<RequestDocModel> getRequestDocumentsById(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("getAllCustomerUploadedDocs")
    Call<MyAllDocListModel> getAllCustomerUploadedDocs(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("deleteRequestDocumentByReqDocId")
    Call<CommonModel> deleteRequestDocumentByReqDocId(@Header("Authorization") String token, @Body String body);

    @Multipart
    @POST("uploadRequesterDocument")
    Call<AttachedFileUploadResponseModel> uploadRequesterDocument(@Header("Authorization") String token,
                                                                  @Part MultipartBody.Part file,
                                                                  @Part("userType") String userType,
                                                                  @Part("RequestId") String RequestId,
                                                                  @Part("documentName") String documentName,
                                                                  @Part("userId") String userId,
                                                                  @Part("isTempRequest") Boolean isTempRequest,
                                                                  @Part("dateTime") String dateTime,
                                                                  @Part("tempRequestId") String tempRequestId,
                                                                  @Part("isDewDoc") Boolean isDewDoc);

    @Headers("Content-Type: application/json")
    @POST("updateExistingRequestDocument")
    Call<CommonModel> updateExistingRequestDocument(@Header("Authorization") String token,
                                                                          @Body String data);

    @Headers("Content-Type: application/json")
    @POST("getRequestParticipantByReqIdAndUserId")
    Call<SignerSignatureInitialAuthorizationModel> getRequestParticipantByReqIdAndUserId(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("updateRequestParticipantCapture")
    Call<CommonModel> updateRequestParticipantCapture(@Header("Authorization") String token, @Body String body);

    @Multipart
    @POST("removeImageBackgroundAndAutoCrop")
    Call<ResponseBody> removeImageBackgroundAndAutoCrop(@Header("Authorization") String token,
                                                        @Part MultipartBody.Part image);

    @Headers("Content-Type: application/json")
    @POST("uploadCustomerImages")
    Call<ResponseBody> uploadCustomerImages(@Header("Authorization") String token, @Body String body);

}
