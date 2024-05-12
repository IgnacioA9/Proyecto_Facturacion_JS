package una.ac.cr.presentation;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api/detalle")
public class detalle {
    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar detalle
     *-Crear PDF
     *-Crear XML
     */

    @GetMapping
    public List<Facturas> read(@AuthenticationPrincipal UserDetailsImp user){
        return service.facturassearchbyproveedor(user.getUsername());
    }
    /*
    @PostMapping()
    public void PDF(@AuthenticationPrincipal UserDetailsImp user,@PathVariable Integer numero){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // Agregar título "Facturas"
            document.add(new Paragraph("Facturas").setFontSize(20).setBold().setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Detalle de la factura"));
            document.add(new Paragraph("Fecha de la factura: " + service.facturassearch(numero).getFecha()));

            // Agregar número de factura y nombre del cliente
            document.add(new Paragraph("Número de factura: " + service.facturassearch(numero).getNumero()));
            document.add(new Paragraph("Cedula Proveedor: " + service.proveedoresread(user.getUsername()).getCedula()));
            document.add(new Paragraph("Nombre Proveedor: " + service.proveedoresread(user.getUsername()).getNombre()));
            document.add(new Paragraph("Cliente: " + service.clientesPorFactura(numero)));
            document.add(new Paragraph("Correo: " + service.clientesPorFactura(numero).getCorreo()));
            document.add(new Paragraph("Telefono: " + service.clientesPorFactura(numero).getTelefono()));

            // Crear tabla para los productos
            Table table = new Table(2);
            table.addCell("Nombre del Producto");
            table.addCell("Precio");

            // Agregar los productos a la tabla
            for (Productos producto : service.productosPorFactura(numero)) {
                table.addCell(producto.getNombre());
                table.addCell(String.valueOf(producto.getPrecio()));
            }


            document.add(table);

            document.add(new Paragraph("Cantidad: " + service.facturassearch(numero).getCantidad()));
            document.add(new Paragraph("Total: " + service.facturassearch(numero).getMonto()));

            // Agregar número de página
            document.showTextAligned(new Paragraph(String.format("Página %d", pdf.getNumberOfPages())),
                    559, 806, pdf.getNumberOfPages(), TextAlignment.RIGHT, VerticalAlignment.TOP, 0);

            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=detalle_factura.pdf");

            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar errores, por ejemplo, mostrar un mensaje de error
            response.setContentType("text/plain");
            response.getWriter().write("Error al generar el PDF: " + e.getMessage());

        }
    }

    @PostMapping()
    public void XML(@AuthenticationPrincipal UserDetailsImp user,@PathVariable Integer numero)throws Exception{
        Proveedores proveedores = service.proveedoresread(user.getUsername());
        Facturas factura = service.facturassearch(numero);
        Adquiere adquiere = service.relacionPorFactura(factura.getNumero());
        Clientes cliente = service.clientesPorFactura(adquiere.getNumerofac());
        List<Productos> productos = service.productosPorFactura(factura.getNumero());
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(new AnnotationConfigApplicationContext());
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".xml");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode(TemplateMode.XML);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        Context cfc = new Context();
        cfc.setVariable("facturas",factura);
        cfc.setVariable("proveedor",proveedores);
        cfc.setVariable("clientes",cliente);
        cfc.setVariable("productos",productos);
        String xml = engine.process("presentation/facturas/xmlView",cfc);
        res.setContentType("application/xml");
        PrintWriter writer = res.getWriter();
        writer.println(xml);
        writer.close();
    }
    */
}
