package com.example.application;

import com.example.application.model.PairedEmployeesPerProject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CSVReaderService {

    String printHelloWorld();

    List<PairedEmployeesPerProject> readCvsFile(MultipartFile file) throws IOException;
}
