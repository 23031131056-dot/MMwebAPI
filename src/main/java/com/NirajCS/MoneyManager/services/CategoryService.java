package com.NirajCS.MoneyManager.services;


import java.util.List;

import org.springframework.stereotype.Service;
import com.NirajCS.MoneyManager.dto.CategoryDTO;
import com.NirajCS.MoneyManager.entity.CategoryEntity;
import com.NirajCS.MoneyManager.entity.ProfileEntity;
import com.NirajCS.MoneyManager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileServices profileServices;
    private final CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileServices.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("Category name already exists");
        }
        CategoryEntity newcategory = toEntity( categoryDTO,  profile);
        CategoryEntity savedCategory = java.util.Objects.requireNonNull(categoryRepository.save(newcategory), "Saved category is null");
        return toDTO(savedCategory);
    }

    //helper method
    private CategoryEntity toEntity(CategoryDTO categoryDTO,ProfileEntity profile){
        return CategoryEntity.builder()
        .name(categoryDTO.getName())
        .icon(categoryDTO.getIcon())
        .profile(profile)
        .type(categoryDTO.getType())
        .build();
    }

    private CategoryDTO toDTO(CategoryEntity entity){
        return CategoryDTO.builder()
        .id(entity.getId())
        .profileId(entity.getProfile() !=null ? entity.getProfile().getId():null)
        .name(entity.getName())
        .icon(entity.getIcon())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .type(entity.getType())
        .build();
    }
    //get category for current user
    public List<CategoryDTO> getCategoriesForCurrentUser(){

        ProfileEntity profile = profileServices.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }
    //get category by type for current user
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type , profile.getId());
        return entities.stream().map(this::toDTO).toList();

    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto){
        ProfileEntity profile = profileServices.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
            .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);

    }
}
