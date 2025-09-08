# Resolving Maven PKIX Errors in a Corporate Environment

This document outlines the steps taken to resolve the `PKIX path building failed` error encountered when running `./mvnw quarkus:dev`. This error typically occurs when building a Java project on a corporate network that uses an SSL-inspecting proxy.

## 1. Understanding the Problem

The root cause of the `PKIX path building failed` error was that the Java Virtual Machine (JVM) running the Maven build did not trust the SSL certificate presented by the corporate proxy. When Maven attempted to download dependencies from a secure repository (like Maven Central), the proxy intercepted the connection and presented its own certificate. Since this certificate was not in the JVM's default trust store, the connection failed with a security error.

## 2. Solution Steps

We resolved this issue by creating a custom trust store containing the corporate root certificate and configuring the Maven wrapper to use it.

### Step 1: Export the Corporate SSL Certificate

First, we obtained the public certificate from the corporate proxy.

1.  **Navigated** to `https://repo.maven.apache.org/maven2/` in a web browser (Chrome/Edge).
2.  **Opened** the certificate details by clicking the padlock icon in the address bar.
3.  **Exported** the certificate as a **Base-64 encoded X.509 (.CER)** file and saved it as `corporate.crt` in the project's root directory.

### Step 2: Create a Custom Trust Store

Next, we used Java's `keytool` utility to create a new trust store (`.truststore.jks`) and import the exported certificate into it.

The following command was run from the project's root directory:

```bash
keytool -import -alias corporate-cert -keystore ./.truststore.jks -file corporate.crt -storepass changeit -noprompt
```

-   This created a new file named `.truststore.jks` containing the trusted corporate certificate.

### Step 3: Configure the Maven Wrapper

Finally, we configured the Maven wrapper to use this new trust store by creating and configuring the `.mvn/jvm.config` file.

1.  We created the file `.mvn/jvm.config`.
2.  We added the following lines to it to point to the new trust store:

    ```
    -Djavax.net.ssl.trustStore=./.truststore.jks
    -Djavax.net.ssl.trustStorePassword=changeit
    ```

With these changes, the Maven wrapper's JVM was able to validate the proxy's certificate, and the build could successfully download dependencies.
