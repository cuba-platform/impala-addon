# CUBA Impala Add-on

## Overview

The add-on provides an ability to use Impala as additional datastore in CUBA-based applications.

## Compatibility with platform versions

| Add-on        | Platform      |
|:------------- |:------------- |
| 1.0-SNAPSHOT  | 6.10-SNAPSHOT |

## Installation and configuration

1. Add custom application component to your project (change the version part if needed):

    `com.haulmont.addon.impala:impala-global:1.0-SNAPSHOT`
  
2. Add a custom [data store](https://doc.cuba-platform.com/manual-6.10/data_store.html) using Studio. In the _Data store_ dialog, enter some name, e.g. "myImpala", select _RdbmsStore_ and leave _Database type_ as is (it will be changed manually, see below). Save the project properties and go to the IDE. Configure additional properties:
    - In `app.properties`, change the DBMS type to `impala`:
    
        `cuba.dbmsType_myImpala = impala`
        
    - Add in both `app.properties` and `web-app.properties`:
    
        `cuba.disableEscapingLikeForDataStores = myImpala`
    
3. Download Impala JDBC driver from [Cloudera](https://www.cloudera.com/downloads/connectors/impala/jdbc/2-5-43.html).
4. Copy driver JARs to the `tomcat/shared/lib` directory
5. Edit datasource configuration in the `context.xml` file, for example:
```
<Resource driverClassName="com.cloudera.impala.jdbc41.Driver"
              maxIdle="2"
              maxTotal="20"
              maxWaitMillis="5000"
              name="jdbc/myImpala"
              type="javax.sql.DataSource"
              url="jdbc:impala://192.168.66.11:21050/impala_kudu"
              validationQuery="SELECT 1"
              username="cloudera"
              password="cloudera"/>
```              

## Usage

- Entities should extends `BaseGenericIdEntity` class. 
- Entities should have specified `@Id` property.
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

- Create/Update operations are supported only for Kudu tables. 
- Optimistic locking (via @Version field) doesn't work.
- In generic filter, IN and NOT IN conditions don't work for String type. JDBC driver escapes some chars e.g. `.` and search doesn't work.
- List of the supported database types: https://kudu.apache.org/docs/schema_design.html. CLOB/LOB database types aren't supported.
