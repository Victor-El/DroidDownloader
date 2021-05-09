[![](https://jitpack.io/v/Victor-El/DroidDownloader.svg)](https://jitpack.io/#Victor-El/DroidDownloader)
![Android CI](https://github.com/Victor-El/DroidDownloader/workflows/Android%20CI/badge.svg)

# DroidDownloader
A simple file downloader android library written in kotlin for modern android apps

## INSTALLATION

### Gradle
Add the JitPack repository to you project level build.gradle file
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency to your module build.gradle file
```groovy
dependencies {
  implementation 'com.github.Victor-El:DroidDownloader:v0.1.1-alpha.0'
}
```

### Maven
Add the JitPack repository
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Add the dependency
```xml
<dependency>
  <groupId>com.github.Victor-El</groupId>
  <artifactId>DroidDownloader</artifactId>
  <version>0.1.1-alpha.0</version>
</dependency>
```

## USAGE

#### Note: Depending on the download location add the neccessary permissions

### Getting Downloader instance
```kt
val downloader: DroidDownloader = DefaultDroidDownloader.Builder()
            .addConnectTimeOutMillis(5_000)
            .addWriteTimeOutMillis(30_000)
            .build()
```

### Downloading a file
```kt
suspend fun downloadFileAsync(
        url: String,
        path: String,
        filename: String,
    ) {
        val status = downloader.downloadFileWithBearerAuth(url, path, filename)
        completedTasks.add(status)
        withContext(Dispatchers.Main) {
            when (status) {
                is DownloadStatus.Success -> downloadFlow.emit(DownloadResult.Success(status.filePath))
                is DownloadStatus.Failure.NetworkFailure -> downloadFlow.emit(DownloadResult.Error(status.message))
                is DownloadStatus.Failure.DiskFailure -> downloadFlow.emit(DownloadResult.Error(status.message))
            }
        }
    }
```

### Downloading a file with authorization
```kt
suspend fun downloadFileAsync(
        url: String,
        path: String,
        filename: String,
        authBearerToken: String,
    ) {
        val status = downloader.downloadFileWithBearerAuth(url, path, filename, authBearerToken)
        completedTasks.add(status)
        withContext(Dispatchers.Main) {
            when (status) {
                is DownloadStatus.Success -> downloadFlow.emit(DownloadResult.Success(status.filePath))
                is DownloadStatus.Failure.NetworkFailure -> downloadFlow.emit(DownloadResult.Error(status.message))
                is DownloadStatus.Failure.DiskFailure -> downloadFlow.emit(DownloadResult.Error(status.message))
            }
        }
    }
```

## Author
Elezua Victor

## License
This project is licensed under the Apache MIT License - See: https://github.com/Victor-El/DroidDownloader/blob/master/LICENSE
