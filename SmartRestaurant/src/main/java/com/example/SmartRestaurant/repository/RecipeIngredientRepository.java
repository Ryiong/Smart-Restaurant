package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.RecipeIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientEntity, Long> {
    List<RecipeIngredientEntity> findAllByRecipeId(Long recipeId);

    boolean existsByRecipeIdAndIngredientIdIn(Long recipeId, Set<Long> requestIngredientId);

    List<RecipeIngredientEntity> findAllByIdInAndRecipeId(Set<Long> requestRecipeIngredientId, Long recipeId);

}
