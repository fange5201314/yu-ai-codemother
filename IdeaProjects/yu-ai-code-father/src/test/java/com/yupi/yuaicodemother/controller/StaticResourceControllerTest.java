package com.yupi.yuaicodemother.controller;

import com.yupi.yuaicodemother.constant.AppConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StaticResourceControllerTest {

    private StaticResourceController staticResourceController;
    private String testDeployKey;
    private Path testDeployDir;

    @BeforeEach
    void setUp() {
        staticResourceController = new StaticResourceController();
        testDeployKey = "test_deploy_key";
        testDeployDir = Paths.get(AppConstant.CODE_DEPLOY_ROOT_DIR, testDeployKey);
    }

    @Test
    void serveStaticResource_DirectoryRedirect() {
        // 测试目录访问重定向（不含斜杠）
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/static/" + testDeployKey);
        request.setAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping",
                "/static/" + testDeployKey);

        ResponseEntity<Resource> response = staticResourceController.serveStaticResource(testDeployKey, request);

        // 验证返回 301 重定向
        assertEquals(301, response.getStatusCode().value());
        assertNotNull(response.getHeaders().get("Location"));
        assertTrue(response.getHeaders().get("Location").get(0).endsWith("/"));
    }

    @Test
    void serveStaticResource_RootIndexHtml() {
        try {
            // 创建测试目录和文件
            Path indexPath = testDeployDir.resolve("index.html");
            Files.createDirectories(indexPath.getParent());
            Files.write(indexPath, "<html><body>Hello World</body></html>".getBytes());

            // 测试根目录访问返回 index.html
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/static/" + testDeployKey + "/");
            request.setAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping",
                    "/static/" + testDeployKey + "/");

            ResponseEntity<Resource> response = staticResourceController.serveStaticResource(testDeployKey, request);

            // 验证返回 200 和正确的 Content-Type
            assertEquals(200, response.getStatusCode().value());
            assertEquals("text/html; charset=UTF-8", response.getHeaders().getFirst("Content-Type"));
            assertNotNull(response.getBody());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        } finally {
            // 清理测试文件
            cleanupTestFiles();
        }
    }

    @Test
    void serveStaticResource_FileNotFound() {
        // 测试文件不存在的情况
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/static/" + testDeployKey + "/nonexistent.html");
        request.setAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping",
                "/static/" + testDeployKey + "/nonexistent.html");

        ResponseEntity<Resource> response = staticResourceController.serveStaticResource(testDeployKey, request);

        // 验证返回 404
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void serveStaticResource_CssFile() {
        try {
            // 创建测试CSS文件
            Path cssPath = testDeployDir.resolve("style.css");
            Files.createDirectories(cssPath.getParent());
            Files.write(cssPath, "body { color: red; }".getBytes());

            // 测试CSS文件访问
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/static/" + testDeployKey + "/style.css");
            request.setAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping",
                    "/static/" + testDeployKey + "/style.css");

            ResponseEntity<Resource> response = staticResourceController.serveStaticResource(testDeployKey, request);

            // 验证返回 200 和正确的 Content-Type
            assertEquals(200, response.getStatusCode().value());
            assertEquals("text/css; charset=UTF-8", response.getHeaders().getFirst("Content-Type"));
            assertNotNull(response.getBody());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        } finally {
            // 清理测试文件
            cleanupTestFiles();
        }
    }

    @Test
    void serveStaticResource_JsFile() {
        try {
            // 创建测试JS文件
            Path jsPath = testDeployDir.resolve("app.js");
            Files.createDirectories(jsPath.getParent());
            Files.write(jsPath, "console.log('test');".getBytes());

            // 测试JS文件访问
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/static/" + testDeployKey + "/app.js");
            request.setAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping",
                    "/static/" + testDeployKey + "/app.js");

            ResponseEntity<Resource> response = staticResourceController.serveStaticResource(testDeployKey, request);

            // 验证返回 200 和正确的 Content-Type
            assertEquals(200, response.getStatusCode().value());
            assertEquals("application/javascript; charset=UTF-8", response.getHeaders().getFirst("Content-Type"));
            assertNotNull(response.getBody());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        } finally {
            // 清理测试文件
            cleanupTestFiles();
        }
    }

    private void cleanupTestFiles() {
        try {
            // 删除测试目录
            File testDir = testDeployDir.toFile();
            if (testDir.exists()) {
                deleteRecursively(testDir);
            }
        } catch (Exception e) {
            // 忽略清理错误
        }
    }

    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }
}