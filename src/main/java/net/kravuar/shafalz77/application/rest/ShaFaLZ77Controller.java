package net.kravuar.shafalz77.application.rest;

import lombok.AllArgsConstructor;
import net.kravuar.shafalz77.application.props.AppProps;
import net.kravuar.shafalz77.application.services.LZ77;
import net.kravuar.shafalz77.application.services.ShaFa;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.Objects;

@RestController
@CrossOrigin(value = "http://localhost:3000", exposedHeaders = HttpHeaders.CONTENT_DISPOSITION)
@AllArgsConstructor
public class ShaFaLZ77Controller {
    private final AppProps appProps;
    private final ShaFa shaFa;
    private final LZ77 lz77;

    @PostMapping(value = "/compress",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> compress(@RequestParam MultipartFile file) {
        var result = new ByteArrayOutputStream(appProps.getBufferSize());
        try {
            var bs = new BufferedInputStream(file.getInputStream());

            if (!shaFa.compress(bs, new BufferedOutputStream(result)))
                return badRequest("Hmmmm... Cho to ne to.");
            var temp = new ByteArrayInputStream(result.toByteArray());
            result.reset();
            if (!lz77.compress(new BufferedInputStream(temp), new BufferedOutputStream(result)))
                return badRequest("Hmmmm... Cho to ne to.");

            return wrapInFile(result, file.getOriginalFilename() + ".sjatoe");
        } catch (IOException e) {
            e.printStackTrace();
            return badRequest(e.getMessage());
        }
    }
    @PostMapping(value = "/decompress", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> decompress(@RequestParam MultipartFile file) {
        ByteArrayOutputStream result = new ByteArrayOutputStream(appProps.getBufferSize());
        try {
            var bs = new BufferedInputStream(file.getInputStream());

            if (!lz77.decompress(bs, new BufferedOutputStream(result)))
                return badRequest("Tebe zdes ne radi");
            var temp = new ByteArrayInputStream(result.toByteArray());
            result.reset();
            if (!shaFa.decompress(new BufferedInputStream(temp), new BufferedOutputStream(result)))
                return badRequest("Tebe zdes ne radi");

            return wrapInFile(result, Objects.requireNonNull(file.getOriginalFilename()).replace("sjatoe", ""));
        } catch (IOException | InvalidParameterException e) {
            e.printStackTrace();
            return badRequest(e.getMessage());
        }
    }

    private ResponseEntity<?> wrapInFile(ByteArrayOutputStream bytes, String filename) {
        ByteArrayResource resource = new ByteArrayResource(bytes.toByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build().toString())
                .body(resource);
    }
    private ResponseEntity<?> badRequest(String msg) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(msg);
    }
}
