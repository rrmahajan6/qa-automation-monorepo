using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using GraphQLProductApp.Data;
using GraphQLProductApp.Repository;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace GraphQLProductApp.Controllers;

[ApiController]
[Authorize]
[Route("[controller]")]
public class ProductController : BaseController
{
    private const string folderName = "files";
    private readonly IProductRepository _productRepository;
    private readonly string folderPath = Path.Combine(Directory.GetCurrentDirectory(), folderName);


    public ProductController(IProductRepository productRepository)
    {
        _productRepository = productRepository;
        if (!Directory.Exists(folderPath)) Directory.CreateDirectory(folderPath);
    }

    [HttpGet]
    [Route("/[controller]/[action]/{id}")]
    public Product GetProductById(int id)
    {
        return _productRepository.GetProductById(id);
    }


    [HttpGet]
    [Route("/[controller]/[action]")]
    public Product GetProductByIdAndName([FromQuery] QueryParameter queryParameter)
    {
        return _productRepository.GetProductByIdAndName(queryParameter.Id, queryParameter.Name);
    }

    [HttpGet]
    [Route("/[controller]/[action]/{name}")]
    public Product GetProductByName(string name)
    {
        return _productRepository.GetProductByName(name);
    }

    // GET: ProductController/GetProducts
    [HttpGet]
    [Route("/[controller]/[action]")]
    public ActionResult<List<Product>> GetProducts()
    {
        return _productRepository.GetAllProducts();
    }

    // POST: ProductController/Create
    [HttpPost]
    [Route("/[controller]/[action]")]
    public Product Create(Product product)
    {
        return _productRepository.AddProduct(product);
    }


    [HttpPost]
    [Consumes("multipart/form-data")]
    public async Task<IActionResult> Post([FromForm] FileUploadModel model)
    {
        using (var fileContentStream = new MemoryStream())
        {
            await model.MyFile.CopyToAsync(fileContentStream);
            await System.IO.File.WriteAllBytesAsync(Path.Combine(folderPath, model.MyFile.FileName),
                fileContentStream.ToArray());
        }

        return CreatedAtRoute("myFile", new { filename = model.MyFile.FileName }, null);
    }

    [HttpGet("{filename}", Name = "myFile")]
    public async Task<IActionResult> Get([FromRoute] string filename)
    {
        var filePath = Path.Combine(folderPath, filename);
        if (System.IO.File.Exists(filePath))
            return File(await System.IO.File.ReadAllBytesAsync(filePath), "application/octet-stream", filename);
        return NotFound();
    }
}

public class FileUploadModel
{
    public IFormFile MyFile { get; set; }
}