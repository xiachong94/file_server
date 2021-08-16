package com.ryu.file_server.controller;

import com.ryu.file_server.entity.Breadcrumb;
import com.ryu.file_server.entity.PathWrapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = {"/", "/{:^assets.+|(?!assets).*$}/**"})
public class BrowseController {

    @Autowired
    private Path basePath;

    @GetMapping
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        String path = request.getServletPath().substring(1);
        Path file = basePath.resolve(path);
        if (Files.isDirectory(file)) {
            List<String> dirs = new ArrayList<>();
            dirs.add("/");
            dirs.addAll(Arrays.stream(path.split("/")).filter(Strings::isNotBlank).collect(Collectors.toList()));
            Collections.reverse(dirs);
            List<Breadcrumb> breadcrumbs = new ArrayList<>();
            for (int i = 0; i < dirs.size(); i++) {
                breadcrumbs.add(new Breadcrumb(crumb(i), dirs.get(i)));
            }
            Collections.reverse(breadcrumbs);
            Map<Boolean, List<PathWrapper>> pathMap = new HashMap<>();
            Files.list(file).collect(Collectors.partitioningBy(Files::isRegularFile)).forEach((k, v) -> {
                List<PathWrapper> list = new ArrayList<>();
                v.forEach(vv -> {
                    list.add(new PathWrapper(vv));
                });
                pathMap.put(k, list);
            });
            int fileCount = pathMap.get(true).size();
            int direCount = pathMap.get(false).size();
            model.addAttribute("breadcrumbs", breadcrumbs);
            model.addAttribute("pathMap", pathMap);
            model.addAttribute("fileCount", fileCount);
            model.addAttribute("direCount", direCount);
            return "index";
        } else {
            response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getFileName().toString().getBytes(), StandardCharsets.ISO_8859_1));
            Files.copy(file, response.getOutputStream());
            return null;
        }
    }

    private String crumb(Integer count) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < count; i++) {
            str.append("../");
        }
        return str.toString();
    }

    @PostMapping
    public String save(HttpServletRequest request, List<MultipartFile> files) throws IOException {
        String path = request.getServletPath().substring(1);
        for (MultipartFile file : files) {
            Path dest = basePath.resolve(path).resolve(file.getOriginalFilename());
            int i = 0;
            while (Files.exists(dest)) {
                dest = dest.resolveSibling(file.getOriginalFilename() + "." + (++i));
            }
            file.transferTo(dest);
        }
        return "redirect:/" + UriEncoder.encode(path);
    }

    public static void main(String[] args) {
        System.out.println("favicon.ico".matches("^(favicon.+|(?!favicon.ico).*)$"));
    }
}
