# spring-fileservice-project

This project consists of 2 services (server & client), as well as a common API for them.

The server has access to a series of file operations (like listing files from a folder, as well as uploading, downloading and removing them).

After compiling with maven, and executing the FileService application in the server, you can call the following endpoints in the htpp://localhost:8080

List files:
GET: /api/v1/files/list?path={path}

Download file:
GET /api/v1/files?path={}

Upload file:
POST /api/v1/files?path={}, passing a MultipartFile

Remove file:
DELETE /api/v1/files?path={}&recursive={}
