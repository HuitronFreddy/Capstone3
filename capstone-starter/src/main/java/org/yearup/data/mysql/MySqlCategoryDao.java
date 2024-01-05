package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.yearup.models.Product;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    //Freddy
    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List <Category> categories = new ArrayList<>();

        String sql = """
                Select *
                From categories;
                """;

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery()){

            while(row.next()){
                Category category = mapRow(row);
                categories.add(category);
            }
        }
        catch(SQLException ex){
            throw new RuntimeException("Unable to fetch categories. Please try again.", ex);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = """
                SELECT *
                FROM categories
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, categoryId);

            try (ResultSet row = statement.executeQuery()){
                if(row.next()) {
                return mapRow(row);
                }
            }
        }
        catch(SQLException ex){
            throw new RuntimeException("Unable to fetch the category. Please try again.", ex);
        }
        return null;
    }

    //Steeven
    @Override
    public Category create(Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO categories (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }


    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    //Nathan
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
