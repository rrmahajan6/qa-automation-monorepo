using RestSharp;

var client = new RestClient("https://jsonplaceholder.typicode.com");
var request = new RestRequest("posts/1", Method.Get);
var response = await client.ExecuteAsync(request);

Console.WriteLine($"Status: {(int)response.StatusCode}");
Console.WriteLine(response.Content);
