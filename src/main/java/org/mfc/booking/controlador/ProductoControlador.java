package org.mfc.booking.controlador;

import org.mfc.booking.dto.Mensaje;
import org.mfc.booking.dto.ProductoContent;
import org.mfc.booking.dto.ProductoDto;
import org.mfc.booking.entidad.ImageModel;
import org.mfc.booking.servicio.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mfc.booking.constantes.AppConstantes.*;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    @Autowired
    ProductoServicio productoServicio;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listarPageSort")
    public ResponseEntity<ProductoContent> listarProductosContent(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_PAGINA_DEFECTO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_PAGINA_DEFECTO, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordernarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCCION_DEFECTO, required = false) String sortDir) {
        return new ResponseEntity(productoServicio.listarPagSort(pageNo, pageSize, ordernarPor, sortDir), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDto>> listar() {
        List<ProductoDto> list = productoServicio.listar();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProductoDto> obtenerProductoPorId(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(productoServicio.obtenerProductoPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/crear", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> crear(@RequestPart("product") ProductoDto productoDto, @RequestPart("imgFile")MultipartFile[] file) {
        try {
            Set<ImageModel> images = uploadImage(file);
            ProductoDto producto = new ProductoDto(productoDto.getNombre(), productoDto.getDescripcion(), productoDto.getCantidad(), productoDto.getEstado(), images, productoDto.getCodigo());
            ProductoDto productoDtoRespuesta = productoServicio.crear(producto);
            return new ResponseEntity<>(productoDtoRespuesta, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("error en el guardado"), HttpStatus.BAD_REQUEST);
        }
    }
 {}
    public  Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        ImageModel imageModel = null;
        for (MultipartFile file : multipartFiles) {
            imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value ="/modificar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> actualizarUsuario(@RequestPart("product") ProductoDto productoDto, @RequestPart("imgFile")MultipartFile[] file){
        if(!this.productoServicio.existePorId(productoDto.getId()))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        try {
            Set<ImageModel> images = uploadImage(file);
            ProductoDto producto = new ProductoDto(productoDto.getNombre(), productoDto.getDescripcion(), productoDto.getCantidad(), productoDto.getEstado(), images, productoDto.getCodigo());
            ProductoDto productoDtoRespuesta = productoServicio.actualizarProducto(producto, productoDto.getId());
            return new ResponseEntity<>(productoDtoRespuesta, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new Mensaje("error en el guardado"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable(name = "id")long id){
        productoServicio.eliminarProducto(id);
        return new ResponseEntity(new Mensaje("Producto eliminado con exito"), HttpStatus.OK);
    }

}
