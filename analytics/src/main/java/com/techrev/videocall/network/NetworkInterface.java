package com.techrev.videocall.network;


import com.techrev.videocall.models.AttachedFileUploadResponseModel;
import com.techrev.videocall.models.ClientActivePlanDetailsByClientIdModel;
import com.techrev.videocall.models.CommonModel;
import com.techrev.videocall.models.CustomerDisagreeCountModel;
import com.techrev.videocall.models.DeleteMessageResponseModel;
import com.techrev.videocall.models.MeetingDetailsModel;
import com.techrev.videocall.models.MyAllDocListModel;
import com.techrev.videocall.models.NotarizationActionModel;
import com.techrev.videocall.models.RequestDetailsModel;
import com.techrev.videocall.models.RequestDocModel;
import com.techrev.videocall.models.RequestForMeetingRoomModel;
import com.techrev.videocall.models.SearchUserResponse;
import com.techrev.videocall.models.SignerSignatureInitialAuthorizationModel;
import com.techrev.videocall.models.UpdateRequestStatusResponse;
import com.techrev.videocall.models.UploadImageModel;
import com.techrev.videocall.ui.chat.ChatDataModel;
import com.techrev.videocall.ui.cosigner.CosignerDetailsModel;
import com.techrev.videocall.ui.cosigner.CosignerVerificationModel;
import com.techrev.videocall.ui.mydocuments.DocumentsByRequestIdModel;
import com.techrev.videocall.ui.videocallroom.ParticipantDetailsModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getarequest")
    Call<MeetingDetailsModel> getAllMeetingDetails(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestDetailsByRequestId")
    Call<RequestDetailsModel> getRequestDetailsByRequestId(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestparticipant")
    Call<ParticipantDetailsModel> getAllParticipantDetails(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getarequest")
    Call<ResponseBody> getRequestDetails(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getDocumentsByRequestId")
    Call<DocumentsByRequestIdModel> getDocumentsByRequestID(@Header("Authorization") String token, @Body String body);


    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestForMeetingRoom")
    Call<RequestForMeetingRoomModel> getRequestForMeetingRoom(@Header("Authorization") String token, @Body String body);


    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getClientActivePlanDetailsByClientId")
    Call<ClientActivePlanDetailsByClientIdModel> getClientActivePlanDetailsByClientId(@Header("Authorization") String token, @Body String body);

    // Newly Added for Multipart for upload files
    @Multipart
    @Headers({"Origin: https://localhost"})
    @POST("insertRequestImages")
    Call<UploadImageModel> uploadFile(@Header("Authorization") String token,
                                      @Part MultipartBody.Part file,
                                      @Part("file") RequestBody name,
                                      @Part("documentName") String docName,
                                      @Part("isDewDoc") Boolean isDewDocName,
                                      @Part("requestId") String requestId);

    @Multipart
    @Headers({"Origin: https://localhost"})
    @POST("uploadAttachmentFile")
    Call<AttachedFileUploadResponseModel> uploadAttachedFile(@Header("Authorization") String token,
                                                             @Part MultipartBody.Part file,
                                                             @Part("file") RequestBody name,
                                                             @Part("documentName") String docName,
                                                             @Part("isDewDoc") Boolean isDewDocName,
                                                             @Part("uploadedBy") String requestId);


    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateDocumentName")
    Call<ResponseBody> updateDocumentName(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("deleteFileName")
    Call<ResponseBody> deleteFileName(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @GET("downloadFile")
    @Streaming
    Call<ResponseBody> downloadImage(@Header("Authorization") String token,@Query("DocId") String docID);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateRequestParticipant")
    Call<ResponseBody> locationData(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("insertCustomerSharedLocationMobile")
    Call<ResponseBody> shareLocationData(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getCustomer")
    Call<SearchUserResponse> searchUser(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("addCosignerDuringMeeting")
    Call<ResponseBody> addCosignerDuringMeeting(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestCosignerByRequestIdAndUserId")
    Call<SearchUserResponse> checkCosignerExist(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getAllMessagesByMeetingId")
    Call<ChatDataModel> getAllMessagesByMeetingId(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("insertMessageDetails")
    Call<ChatDataModel> saveNewMessage(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("deleteMyMessage")
    Call<DeleteMessageResponseModel> deleteMessage(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestCosignerByRequestId")
    Call<CosignerDetailsModel> getAllCoSignerDetailsByMeetingId(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getCustomerDisagreeCount")
    Call<CustomerDisagreeCountModel> getCustomerDisagreeCount(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateDisagreeCount")
    Call<CustomerDisagreeCountModel> updateDisagreeCount(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateIsCustomerAgreedDetails")
    Call<CustomerDisagreeCountModel> updateAgreeCount(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getUserAccessCodeAndIDMSessionIdByUserId")
    Call<CosignerVerificationModel> checkIfKBADoneOfUserByID(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestCosignerByRequestId")
    Call<CosignerDetailsModel> getCosignerDetailsByRequestId(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateRequestStatusByCustomer")
        Call<UpdateRequestStatusResponse> updateRequestStatusByCustomer(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("notifyServiceProvider")
    Call<UpdateRequestStatusResponse> notifyServiceProvider(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestDocumentsById")
    Call<RequestDocModel> getRequestDocumentsById(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @GET("getAllCustomerUploadedDocs")
    Call<MyAllDocListModel> getAllCustomerUploadedDocs(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("deleteRequestDocumentByReqDocId")
    Call<CommonModel> deleteRequestDocumentByReqDocId(@Header("Authorization") String token, @Body String body);

    @Multipart
    @Headers({"Origin: https://localhost"})
    @POST("uploadRequesterDocument")
    Call<AttachedFileUploadResponseModel> uploadRequesterDocument(@Header("Authorization") String token,
                                                             @Part MultipartBody.Part file,
                                                             @Part("userType") RequestBody userType,
                                                             @Part("documentName") RequestBody documentName,
                                                             @Part("userId") RequestBody userId,
                                                             @Part("dateTime") RequestBody dateTime,
                                                             @Part("isDewDoc") RequestBody isDewDoc,
                                                             @Part("uploadedBy") RequestBody uploadedBy);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateExistingRequestDocument")
    Call<CommonModel> updateExistingRequestDocument(@Header("Authorization") String token,
                                                    @Body String requestObject);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getRequestParticipantByReqIdAndUserId")
    Call<SignerSignatureInitialAuthorizationModel> getRequestParticipantByReqIdAndUserId(@Header("Authorization") String token, @Body String body);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("updateRequestParticipantCapture")
    Call<CommonModel> updateRequestParticipantCapture(@Header("Authorization") String token, @Body String body);

    @Multipart
    @Headers({"Origin: https://localhost"})
    @POST("removeImageBackgroundAndAutoCrop")
    Call<ResponseBody> removeImageBackgroundAndAutoCrop(@Header("Authorization") String token,
                                                        @Part MultipartBody.Part image);

    @Multipart
    @Headers({"Origin: https://localhost"})
    @POST("uploadCustomerImages")
    Call<ResponseBody> uploadCustomerImages(@Header("Authorization") String token,
                                            @Part MultipartBody.Part image,
                                            @Part("isDewDoc") RequestBody ImageType,
                                            @Part("signatureUploadId") RequestBody signatureUploadld,
                                            @Part("isSignature") RequestBody isSignature,
                                            @Part("isEditedlmage") RequestBody isEditedlmage,
                                            @Part("guid") RequestBody guid,
                                            @Part("requestId") RequestBody requestId,
                                            @Part("userId") RequestBody userid
                                            );

    @GET("downloadDewFile")
    @Headers({"Origin: https://localhost"})
    @Streaming // Use Streaming to download large files
    Call<ResponseBody> downloadPdf(@Query("DocId") String docID);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("getAllNotarizationAction")
    Call<NotarizationActionModel> getAllNotarizationAction(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json" , "Origin: https://localhost"})
    @POST("insertNotarizationAuditTrial")
    Call<ResponseBody> insertNotarizationAuditTrial(@Header("Authorization") String token, @Body String data);

}
