# Spring Trading App :: Frontend

> [!NOTE]
> This is one of many components from
> [Spring Trading App](https://github.com/alexandreroman/sta).

This component is the GUI of Spring Trading App.

## Running this component on your workstation

Use this command to run this component on your workstation:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev,secrets
```

The app is available at http://localhost:8080.

Application configuration is defined in `src/main/resources/application.yaml`.
You may want to override this configuration in
`src/main/resources/application-dev.yaml`:

```yaml
app:
  banner:
    url: https://d1.awsstatic.com/partner-network/QuickStart/logos/vmware-tanzu-application-platform-logo.e2e1eaa23c5795f062a3f9acfbc567b9ee20be7a.png
    alt: VMware Tanzu Application Platform
  marketplace:
    url: http://localhost:8081
```

You also need to set up authentication credentials in order to connect
to the Marketplace API.

Create the file `src/main/resources/application-secrets.yaml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          sso:
            client-id: insert-client-id
            client-secret: insert-client-secret
            authorization-grant-type: client_credentials
            scope:
            - frontend
        provider:
          sso:
            issuer-uri: https://login.sso.az.run.withtanzu.com
```

Those OAuth2 credentials must be created beforehand.

When using the OAuth2 Resource Server from Tanzu Application Platform, you
can generate such credentials by creating a `ClientRegistration` entity:

```yaml
apiVersion: sso.apps.tanzu.vmware.com/v1alpha1
kind: ClientRegistration
metadata:
  name: sta-frontend
spec:
  authServerSelector:
    matchLabels:
      sso.apps.tanzu.vmware.com/env: run
  authorizationGrantTypes:
  - client_credentials
  clientAuthenticationMethod: client_secret_basic
  requireUserConsent: false
```

OAuth2 credentials are available in a Kubernetes `Secret`.

## Deploying with VMware Tanzu Application Platform

Use this command to deploy this component to your favorite Kubernetes cluster:

```shell
tanzu apps workload apply -f config/workload.yaml
```

The platform will take care of building, testing and deploying this component.

This component also loads some configuration from a
[Git repository](https://github.com/alexandreroman/sta-config).

Run this command to create a Kubernetes `Secret` out of this Git repository,
which will be used by the component at runtime:

```shell
kubectl apply -f config/app-operator
```

Run this command to get deployment status:

```shell
tanzu apps workload get sta-frontend
```
