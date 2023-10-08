package com.tibirabotto.dscatalog.services;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.tibirabotto.dscatalog.dto.CategoryDTO;
import com.tibirabotto.dscatalog.entities.Category;
import com.tibirabotto.dscatalog.repositories.CategoryRepository;
import com.tibirabotto.dscatalog.services.exceptions.ResourcesNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(@PathVariable Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourcesNotFoundException("Entity not found"));

		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourcesNotFoundException("ID not found" + id);
		}

	}
}
