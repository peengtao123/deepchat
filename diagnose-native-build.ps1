# Spring Boot Native Image 构建诊断脚本
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Spring Boot Native Build 诊断工具" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. 检查 Docker
Write-Host "[1/6] 检查 Docker..." -ForegroundColor Yellow
try {
    $dockerVersion = docker --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✓ Docker 已安装: $dockerVersion" -ForegroundColor Green
        
        # 检查 Docker 是否运行
        $dockerInfo = docker info 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "   ✓ Docker 服务正在运行" -ForegroundColor Green
        } else {
            Write-Host "   ✗ Docker 服务未启动，请先启动 Docker Desktop" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "   ✗ Docker 未安装或未添加到 PATH" -ForegroundColor Red
        Write-Host "   下载地址: https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "   ✗ 检查 Docker 时出错: $_" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 2. 检查网络连接
Write-Host "[2/6] 检查网络连接..." -ForegroundColor Yellow
$testUrls = @(
    @{url="https://github.com"; name="GitHub"},
    @{url="https://repo.maven.apache.org"; name="Maven Central"}
)

foreach ($test in $testUrls) {
    try {
        $response = Invoke-WebRequest -Uri $test.url -TimeoutSec 5 -UseBasicParsing -Method Head
        if ($response.StatusCode -eq 200) {
            Write-Host "   ✓ $($test.name) 可访问" -ForegroundColor Green
        }
    } catch {
        Write-Host "   ✗ $($test.name) 访问失败（可能需要代理）" -ForegroundColor Red
        Write-Host "      错误: $($_.Exception.Message)" -ForegroundColor Gray
    }
}
Write-Host ""

# 3. 检查磁盘空间
Write-Host "[3/6] 检查磁盘空间..." -ForegroundColor Yellow
$disk = Get-PSDrive C
$freeSpaceGB = [math]::Round($disk.Free / 1GB, 2)
if ($freeSpaceGB -gt 10) {
    Write-Host "   ✓ C盘可用空间: ${freeSpaceGB}GB" -ForegroundColor Green
} elseif ($freeSpaceGB -gt 5) {
    Write-Host "   ⚠ C盘可用空间: ${freeSpaceGB}GB（建议 >10GB）" -ForegroundColor Yellow
} else {
    Write-Host "   ✗ C盘可用空间不足: ${freeSpaceGB}GB（需要 >10GB）" -ForegroundColor Red
}
Write-Host ""

# 4. 检查 Maven
Write-Host "[4/6] 检查 Maven..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn --version 2>&1 | Select-String "Apache Maven" | Select-Object -First 1
    if ($mavenVersion) {
        Write-Host "   ✓ $mavenVersion" -ForegroundColor Green
        
        # 检查 Maven 版本
        $versionMatch = $mavenVersion -match "(\d+\.\d+\.\d+)"
        if ($versionMatch) {
            $version = [version]$matches[1]
            if ($version -ge [version]"3.6.0") {
                Write-Host "   ✓ Maven 版本满足要求 (>= 3.6.0)" -ForegroundColor Green
            } else {
                Write-Host "   ⚠ Maven 版本过低，建议升级到 3.6.0+" -ForegroundColor Yellow
            }
        }
    } else {
        Write-Host "   ✗ Maven 未安装或版本检测失败" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ Maven 未安装或未添加到 PATH" -ForegroundColor Red
    Write-Host "   下载地址: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
}
Write-Host ""

# 5. 检查 Java
Write-Host "[5/6] 检查 Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    if ($javaVersion) {
        Write-Host "   ✓ Java 版本: $javaVersion" -ForegroundColor Green
        
        # 检查是否为 GraalVM
        $isGraalVM = java -version 2>&1 | Select-String "GraalVM"
        if ($isGraalVM) {
            Write-Host "   ✓ 检测到 GraalVM（可用于本地原生构建）" -ForegroundColor Green
        } else {
            Write-Host "   ℹ 使用标准 JDK（将使用 Buildpacks 构建）" -ForegroundColor Cyan
        }
        
        # 检查 Java 版本
        $versionMatch = $javaVersion -match "(\d+)"
        if ($versionMatch) {
            $majorVersion = [int]$matches[1]
            if ($majorVersion -ge 17) {
                Write-Host "   ✓ Java 版本满足要求 (>= 17)" -ForegroundColor Green
            } else {
                Write-Host "   ✗ Java 版本过低，需要 >= 17" -ForegroundColor Red
            }
        }
    } else {
        Write-Host "   ✗ Java 未安装或未添加到 PATH" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ 检查 Java 时出错: $_" -ForegroundColor Red
}
Write-Host ""

# 6. 检查项目配置
Write-Host "[6/6] 检查项目配置..." -ForegroundColor Yellow
$pomPath = Join-Path $PSScriptRoot "pom.xml"
if (Test-Path $pomPath) {
    Write-Host "   ✓ pom.xml 存在" -ForegroundColor Green
    
    # 检查是否包含 spring-boot-maven-plugin
    $pomContent = Get-Content $pomPath -Raw
    if ($pomContent -match "spring-boot-maven-plugin") {
        Write-Host "   ✓ 包含 spring-boot-maven-plugin" -ForegroundColor Green
    } else {
        Write-Host "   ✗ 未找到 spring-boot-maven-plugin" -ForegroundColor Red
    }
    
    # 检查 Spring Boot 版本
    if ($pomContent -match "<version>4\.0\.\d+</version>") {
        Write-Host "   ✓ Spring Boot 4.0.x 版本" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Spring Boot 版本可能不是 4.0.x" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ✗ pom.xml 不存在" -ForegroundColor Red
}
Write-Host ""

# 检查 Docker 镜像缓存
Write-Host "[额外检查] Docker 镜像缓存..." -ForegroundColor Yellow
try {
    $builderImage = docker images paketobuildpacks/builder-jammy-base --format "{{.Repository}}:{{.Tag}}" 2>&1
    if ($builderImage -match "paketobuildpacks/builder-jammy-base") {
        Write-Host "   ✓ Builder 镜像已缓存" -ForegroundColor Green
    } else {
        Write-Host "   ℹ Builder 镜像未缓存（首次构建会下载，约 1-2GB）" -ForegroundColor Cyan
    }
} catch {
    Write-Host "   ⚠ 无法检查 Docker 镜像" -ForegroundColor Yellow
}
Write-Host ""

# 总结和建议
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  诊断完成 - 建议操作" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "如果所有检查都通过，可以尝试以下命令构建：" -ForegroundColor White
Write-Host ""
Write-Host "  # 方法 1: 使用 Buildpacks（推荐）" -ForegroundColor Cyan
Write-Host "  mvn spring-boot:build-image" -ForegroundColor Gray
Write-Host ""
Write-Host "  # 方法 2: 增加内存和超时时间" -ForegroundColor Cyan
Write-Host '  $env:MAVEN_OPTS="-Xmx4g"' -ForegroundColor Gray
Write-Host "  mvn spring-boot:build-image" -ForegroundColor Gray
Write-Host ""
Write-Host "  # 方法 3: 使用本地 GraalVM（如果已安装）" -ForegroundColor Cyan
Write-Host "  mvn -Pnative clean package" -ForegroundColor Gray
Write-Host ""

Write-Host "如果仍然失败，请参考:" -ForegroundColor White
Write-Host "  NATIVE_BUILD_TROUBLESHOOTING.md" -ForegroundColor Yellow
Write-Host ""

Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
