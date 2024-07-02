package ec.tecAzuayM5a.cuencananandroid.modelo;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // populate data byte payload
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }

            // close multipart form data after text and file data
            dos.writeBytes("--" + boundary + "--" + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void textParse(DataOutputStream dos, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void dataParse(DataOutputStream dos, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dos, entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dos, String parameterName, String parameterValue) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes(parameterValue + "\r\n");
    }

    private void buildDataPart(DataOutputStream dos, DataPart dataFile, String inputName) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + dataFile.getFileName() + "\"\r\n");
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dos.writeBytes("Content-Type: " + dataFile.getType() + "\r\n");
        }
        dos.writeBytes("\r\n");

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dos.writeBytes("\r\n");
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart() {
        }

        public DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        public DataPart(String name, byte[] data, String type) {
            fileName = name;
            content = data;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
