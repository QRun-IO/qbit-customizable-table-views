# qbit-customizable-table-views

User-defined saved views for QQQ dashboard tables.

**For:** QQQ developers who want users to save and share custom table configurations  
**Status:** Stable

## Why This Exists

Different users need different views of the same data. Sales wants to see orders by region. Operations wants orders by status. Each applies different filters, sorts columns differently, and hides irrelevant fields.

This QBit lets users save these configurations as named views. Create a view, adjust columns and filters, save it, and switch between views instantly. Views can be private or shared with the team.

## Features

- **Saved Views** - Store filter, sort, and column configurations
- **Column Visibility** - Show/hide columns per view
- **Column Order** - Reorder columns with drag-and-drop
- **Filter Presets** - Save complex filter combinations
- **View Sharing** - Share views with roles or all users
- **Default Views** - Set a view as the default for a table

## Quick Start

### Prerequisites

- QQQ application (v0.20+)
- QQQ dashboard deployed
- Database backend configured

### Installation

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>io.qrun</groupId>
    <artifactId>qbit-customizable-table-views</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Register the QBit

```java
public class AppMetaProvider extends QMetaProvider {
    @Override
    public void configure(QInstance qInstance) {
        new CustomizableTableViewsQBit().configure(qInstance);
    }
}
```

### Enable for Tables

```java
new QTableMetaData()
    .withName("order")
    .withCustomViewsEnabled(true);
```

## Usage

### Dashboard Experience

Users interact with views through the dashboard:

1. Navigate to a table
2. Adjust columns, filters, and sorting
3. Click "Save View"
4. Name the view and set visibility (private/shared)
5. Switch views from the dropdown

### Programmatic View Creation

```java
new QSavedViewMetaData()
    .withName("pendingOrders")
    .withTable("order")
    .withLabel("Pending Orders")
    .withFilter(new QQueryFilter()
        .withCriteria("status", Operator.EQUALS, "pending"))
    .withColumns("orderNumber", "customerName", "total", "createdDate")
    .withSortBy("createdDate", SortOrder.DESC)
    .withSharedWithRoles("sales", "operations");
```

### Default Views

Set a view as the default for all users or specific roles:

```java
new QSavedViewMetaData()
    .withName("activeCustomers")
    .withTable("customer")
    .withIsDefault(true)
    .withFilter(new QQueryFilter()
        .withCriteria("status", Operator.EQUALS, "active"));
```

### View Inheritance

Create views that extend others:

```java
new QSavedViewMetaData()
    .withName("myPendingOrders")
    .withExtendsView("pendingOrders")
    .withAdditionalFilter(new QQueryFilter()
        .withCriteria("assignedToUserId", Operator.EQUALS, "${currentUserId}"));
```

## Configuration

The QBit creates these tables:

| Table | Purpose |
|-------|---------|
| `saved_view` | View definitions |
| `saved_view_column` | Column configurations |
| `saved_view_share` | Sharing permissions |

### View Limits

```java
new CustomizableTableViewsQBit()
    .withMaxViewsPerUser(50)
    .withMaxSharedViewsPerTable(20);
```

## Project Status

Stable and production-ready.

### Roadmap

- View folders/categories
- View usage analytics
- Export/import view configurations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Run tests: `mvn clean verify`
4. Submit a pull request

## License

Proprietary - QRun.IO
