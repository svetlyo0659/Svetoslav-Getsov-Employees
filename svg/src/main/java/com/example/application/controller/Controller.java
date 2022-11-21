package com.example.application.controller;

import com.example.application.CSVReaderService;
import com.example.application.annotation.FileNotEmpty;
import com.example.application.model.PairedEmployeesPerProject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller
{
    private static final String TEST_URL = "/api/v1/test";
    private static final String READ_URL = "/api/v1/read";
    private final CSVReaderService csvReaderService;

    @GetMapping(TEST_URL)
    @ResponseStatus(HttpStatus.OK)
    @SneakyThrows
    public String returnString()
    {
        return csvReaderService.printHelloWorld();
    }

    @PostMapping(value = READ_URL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                                                 MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
//    @ExtensionCheck(param = "multipartFile", allowedTypes = {".csv"})
    public List<PairedEmployeesPerProject> readFile(@RequestPart("file") @FileNotEmpty MultipartFile multipartFile)
            throws IOException
    {
        return csvReaderService.readCvsFile(multipartFile);
    }
}
