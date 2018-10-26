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
5. Configure datasource in the `context.xml` file, for example:
```
<Resource driverClassName="com.cloudera.impala.jdbc41.Driver"
              maxIdle="2"
              maxTotal="20"
              maxWaitMillis="5000"
              name="jdbc/{store_name}"
              type="javax.sql.DataSource"
              url="jdbc:impala://192.168.66.11:21050/impala_kudu"
              validationQuery="SELECT 1"
              username="cloudera"
              password="cloudera"/>
```              

## Usage
- JPA entities should extends `BaseGenericIdEntity` class. 
- JPA entities should have specified `@Id` property.
For example: 
```
@NamePattern("%s|productName")
@Table(name = "PRODUCTS")
@Entity(name = "impala$Product")
public class Product extends BaseGenericIdEntity<Integer> {
    private static final long serialVersionUID = -9203546219749413634L;

    @Id
    @Column(name = "PRODUCT_ID")
    protected Integer id;

    @Column(name = "PRODUCT_NAME")
    protected String productName;

    @Column(name = "PRODUCT_PRICE")
    protected Double productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_TYPE_ID")
    protected ProductType productType;

    @Column(name = "DATE")
    protected Date date;
}
```
## Limitations 
- Create/Update operations are supported only for KUDU tables. 
- JPA entities with optimistic locking (have @Version field) doesn't work
- IN and NOT IN conditions in the filter don't work for String type. JDBC driver escapes some chars e.g. `.` and search doesn't work.
- List of the supported database types: https://kudu.apache.org/docs/schema_design.html. CLOB/LOB database types aren't supported.

## Forums
* [Cuba Platform](https://www.cuba-platform.com/support/)
