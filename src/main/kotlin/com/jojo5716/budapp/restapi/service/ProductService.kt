package com.jojo5716.budapp.restapi.service

import com.jojo5716.budapp.restapi.dao.ProductDAO
import com.jojo5716.budapp.restapi.domain.Product
import com.jojo5716.budapp.restapi.utils.update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.context.WebApplicationContext
import javax.persistence.EntityNotFoundException

@Service
class ProductService : BasicCRUD<Product, String> {

    @Autowired
    private lateinit var productDao: ProductDAO

    override fun findAll(): List<Product> = this.productDao.findAll()

    override fun findById(id: String): Product? = productDao.findByIdOrNull(id)

    override fun save(t: Product): Product {
        if (this.productDao.existsById("${t.id}")) {
            throw DuplicateKeyException("${t.name} does exist")
        }
        return this.productDao.save(t)
    }

    override fun update(t: Product): Product {
        if (this.productDao.existsById("${t.id}")) {
            return this.productDao.save(t)
        }

        throw DuplicateKeyException("${t.name} does exist")
    }

    override fun deleteById(id: String): Product {
        return this.findById(id)?.apply {
            this@ProductService.productDao.deleteById(id)
        } ?: throw EntityNotFoundException("${id} does not exist")
    }
}