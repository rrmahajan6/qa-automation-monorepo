using FluentAssertions;
using RestSharp;
using Xunit;

namespace RestSharpProject.Tests;

public class Sample
{
    [Fact]
    public async Task Test1()
    {
        var restClientOptions = new RestClientOptions
        {
            BaseUrl = new Uri("https://localhost:5001/"),
            RemoteCertificateValidationCallback = (sender, certificate, chain, sslPolicyErrors) => true
        };
        var client = new RestClient(restClientOptions);
        var request = new RestRequest("Components/GetComponentByProductId/1", Method.Get);
        var response = await client.GetAsync(request);
        ((int)response.StatusCode).Should().Be(200);
        response.Content.Should().NotBeNullOrEmpty();
        Console.WriteLine(response.Content);
    }
}