package com.example.application.extensionCheck;

import com.example.application.exception.FileNotSupportedException;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@CommonsLog
@Component("extensionCheckAspect")
public class ExtensionCheckAspect {


    public void before(JoinPoint jp, ExtensionCheck extensionCheck)
    {
        final String param = extensionCheck.param();
        String[] allowedTypes = extensionCheck.allowedTypes();
        Set<String> allowed = new HashSet<>(Arrays.asList(allowedTypes));

        log.info("Checking if extension is valid for param: " + param);

        File[] files = getParameterByName(jp, param);

        if (Objects.isNull(files)) {
            log.debug("Files not found to check extension");
            return;
        }

        if (0 == allowedTypes.length)
        {
            return;
        }

        for (File file : files) {
            String extension = file.getName().substring(file.getName().lastIndexOf('.'));
            if (!allowed.contains(extension)) {
                throw new FileNotSupportedException("Type of file not allowed: " + extension);
            }
        }
    }

    private File[] getParameterByName(JoinPoint jp, String parameterName)
    {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] params = signature.getParameterNames();

        int idx = Arrays.asList(params).indexOf(parameterName);

        Object[] arguments = jp.getArgs();
        if (idx > -1 && arguments.length > idx) {
            return getFiles(arguments[idx]);
        }

        return new File[0];

    }

    private File[] getFiles(Object obj)
    {
        if (obj instanceof MultipartFile[]) {
            return multipartToFileArray((MultipartFile[]) obj);
        }
        if (obj instanceof MultipartFile) {
            return multipartToFileArray(new MultipartFile[]{(MultipartFile) obj});
        }

        return new File[0];
    }

    private static File[] multipartToFileArray(MultipartFile[] multipart)
    {
        if(null == multipart) {
            return null;
        }

        File[] result = new File[multipart.length];
        int idx = 0;
        for (MultipartFile file: multipart) {
            File convFile = new File(getTempDir() + File.separator + getMod() + file.getOriginalFilename());
            try {
                file.transferTo(convFile);
                result[idx++] = convFile;
            }
            catch (IOException e) {
                log.warn("multipart file can't be transferred to file", e);
            }
        }

        return result;
    }


    public static String getTempDir()
    {
        return System.getProperty("java.io.tmpdir");
    }

    private static String getMod()
    {
        return String.valueOf(System.currentTimeMillis());
    }

}
