using FluentAssertions;
using RestSharp;
using Xunit;

namespace RestSharpProject.Tests;

public class JsonPlaceholderTests
{
    [Fact]
    public async Task Get_Post_1_Should_Return_200_And_Content()
    {
        var client = new RestClient("https://jsonplaceholder.typicode.com");
        var request = new RestRequest("posts/1", Method.Get);

        var response = await client.ExecuteAsync(request);

        ((int)response.StatusCode).Should().Be(200);
        response.Content.Should().NotBeNullOrWhiteSpace();
        response.Content.Should().Contain("\"id\": 1");
    }
}