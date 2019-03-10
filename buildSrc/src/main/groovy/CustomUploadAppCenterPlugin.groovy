import com.google.gson.Gson
import okhttp3.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomUploadAppCenterPlugin implements Plugin<Project> {

    final String APPCENTER_BASE_URL = "https://api.appcenter.ms"

    final String APPCENTER_PATH_BASE_API = "v0.1/apps"
    final String APPCENTER_PATH_RELEASE_UPLOAD = "release_uploads"

    final String APPCENTER_HEADER_CONTENT_TYPE = "Content-Type"
    final String APPCENTER_HEADER_ACCEPT = "Accept"
    final String APPCENTER_HEADER_TOKEN = "X-API-Token"

    final String APPCENTER_MEDIA_TYPE_JSON = "application/json"
    final String APPCENTER_MEDIA_TYPE_MULTI_FORM = "multipart/form-data"
    final String APPCENTER_MEDIA_TYPE_OCTET = "application/octet-stream"

    final String APPCENTER_PART_KEY_ARTIFACT = "ipa"

    OkHttpClient httpClient = new OkHttpClient()
    final String apiToken = "f88d63f25800fd31559850c698a1346002b0c42b"
    final String appOwner = "konst007-tut.by"
    final String appName = "konsttest2"
    File artifact
    final String destination = "Me"
    final String releaseNotes = "This is DEV! Task4 success!"
    final HttpUrl baseUrl = HttpUrl.get(APPCENTER_BASE_URL)

    void apply(Project target) {
        target.task("toAppCenter") {
            doLast {
                artifact = new File("${target.projectDir}/build/outputs/apk/release/KonstTest2.apk")
                InitResponse initResponse = initUpload()
                println "Init Upload"
                upload(initResponse.upload_url)
                CommitResponse commitResponse = commit(initResponse.upload_id)
                distribute(commitResponse.release_url)
            }
        }
    }

    InitResponse initUpload() {
        Request request = new Request.Builder()
                .url(baseUrl.newBuilder()
                .addEncodedPathSegments(APPCENTER_PATH_BASE_API)
                .addEncodedPathSegments(appOwner)
                .addEncodedPathSegments(appName)
                .addEncodedPathSegments(APPCENTER_PATH_RELEASE_UPLOAD)
                .build())
                .addHeader(APPCENTER_HEADER_CONTENT_TYPE, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_ACCEPT, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_TOKEN, apiToken)
                .post(RequestBody.create(MediaType.parse(APPCENTER_MEDIA_TYPE_JSON), ""))
                .build()
        Response response = httpClient.newCall(request).execute()
        if (!response.successful) {
            throw new IllegalStateException(response.message())
        }
        println "Initialized"
        return new Gson().fromJson(response.body().string(), InitResponse.class)
    }

    void upload(String uploadUrl) {
        Request request = new Request.Builder()
                .url(uploadUrl)
                .addHeader(APPCENTER_HEADER_CONTENT_TYPE, APPCENTER_MEDIA_TYPE_MULTI_FORM)
                .post(
                    new MultipartBody.Builder()
                        .addFormDataPart(
                                APPCENTER_PART_KEY_ARTIFACT,
                                artifact.name,
                                RequestBody.create(
                                        MediaType.parse(APPCENTER_MEDIA_TYPE_OCTET),
                                        artifact
                                )
                        )
                        .build()
                )
                .build()

        Response response = httpClient.newCall(request).execute()
        if (!response.isSuccessful()) {
            throw IllegalStateException(response.message())
        } else {
            response.close()
            println "Uploaded"
        }
    }

    CommitResponse commit(String uploadId) {
        Request request = new Request.Builder()
                .url(baseUrl.newBuilder()
                        .addEncodedPathSegments(APPCENTER_PATH_BASE_API)
                        .addEncodedPathSegments(appOwner)
                        .addEncodedPathSegments(appName)
                        .addEncodedPathSegments(APPCENTER_PATH_RELEASE_UPLOAD)
                        .addEncodedPathSegments(uploadId)
                        .build())
                .addHeader(APPCENTER_HEADER_CONTENT_TYPE, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_ACCEPT, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_TOKEN, apiToken)
                .patch(RequestBody.create(
                        MediaType.parse(APPCENTER_MEDIA_TYPE_JSON),
                        new Gson().toJson(new CommitRequest())))
                .build()
        Response response = httpClient.newCall(request).execute()
        if (!response.isSuccessful()) {
            throw IllegalStateException(response.message())
        }
        println "Committed"
        return new Gson().fromJson(response.body().string(), CommitResponse.class)
    }

    void distribute(String releasePath) {
        Request request = new Request.Builder()
                .url(baseUrl.newBuilder()
                        .addEncodedPathSegments(releasePath)
                        .build())
                .addHeader(APPCENTER_HEADER_CONTENT_TYPE, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_ACCEPT, APPCENTER_MEDIA_TYPE_JSON)
                .addHeader(APPCENTER_HEADER_TOKEN, apiToken)
                .patch(RequestBody.create(
                        MediaType.parse(APPCENTER_MEDIA_TYPE_JSON),
                        new Gson().toJson(new DistributionRequest(destination, releaseNotes))))
                .build()

        Response response = httpClient.newCall(request).execute()
        if (!response.isSuccessful()) {
            throw IllegalStateException(response.message())
        } else {
            response.close()
            println "Distributed"
        }
    }
}

class InitResponse {
    String upload_id
    String upload_url
}

class CommitRequest {
    String status = "committed"
}

class CommitResponse {
    String release_id
    String release_url
}

class DistributionRequest {
    String destination_name
    String release_notes

    DistributionRequest(String destination_name, String release_notes) {
        this.destination_name = destination_name
        this.release_notes = release_notes
    }
}