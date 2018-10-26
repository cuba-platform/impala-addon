# CUBA Impala Add-on
## Overview

The add-on adds an ability to use Impala as additional datastore in the CUBA-based
applications.

## Compatibility with platform versions

| Add-on        | Platform      |
|:------------- |:------------- |
| 1.0-SNAPSHOT  | 6.10-SNAPSHOT |

## Installation and configuration

1. Add custom application component to your project (change the version part if needed):

    `com.haulmont.addon.impala:impala-global:1.0-SNAPSHOT`
  
2. Configure additional Impala datastore using https://doc.cuba-platform.com/manual-6.10/data_store.html:
    - Additionally set `cuba.dbmsType_{store_name} = impala`
    - Additionally set `cuba.disableEscapingLikeForDataStores={store_name}`. Property should be specified in the property files of all used application        blocks (`app.properties`, `web-app.properties`, `portal-app.properties`, etc.)  
    
3. Download Impala JDBC driver from the Cloudera https://www.cloudera.com/downloads/connectors/impala/jdbc/2-5-43.html.
4. Copy driver JARs to the `tomcat/shared/lib` directory
5. Configure datasource in the `context.xml` file, e.g.:
```
<Resource driverClassName="com.cloudera.impala.jdbc41.Driver"
              maxIdle="2"
              maxTotal="20"
              maxWaitMillis="5000"
              name="jdbc/ORDERS"
              type="javax.sql.DataSource"
              url="jdbc:impala://192.168.66.11:21050/impala_kudu"
              validationQuery="SELECT 1"
              username="cloudera"
              password="cloudera"/>
```              

## Usage

## Forums
* [Cuba Platform](https://www.cuba-platform.com/support/)
