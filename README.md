# Bitespeed backend task assignment

<b> Instructions to run this application </b>
<ul>
<li> In project root directory run below commands </li>
<li> Build a docker image from Dockerfile <code>sudo docker build -t bitespeed-assignment -f docker/Dockerfile .</code></li>
<li> Run the docker image <code> sudo docker run --rm -p 8080:8080 --name bitespeed bitespeed-assignment</code></li>
</ul>
<br/>
<b> The application exposed following APIs </b>
<table>
<tr>
<th>API</th>
<th>URL</th>
<th>Method</th>
<th>Description</th>
<th>Payload</th>
</tr>
<tr>
<td>Identify Post API</td>
<td><code>http://localhost:8080/identify</code></td>
<td>POST</td>
<td>API to insert contact information.</td>
<td><code>{
    "email":"hemanth@gmail.com",
    "phoneNumber":"8892770064"<br/>
}</code></td>
</tr>
<tr>
<td>Identify Get API</td>
<td><code>http://localhost:8080/identify/{id}</code></td>
<td>GET</td>
<td>It is API to get specific record based on given ID</td>
</tr>
<tr>
<td>Identify Get API</td>
<td><code>http://localhost:8080/identify/</code></td>
<td>GET</td>
<td>API to list all contact information</td>
</tr>
</table>