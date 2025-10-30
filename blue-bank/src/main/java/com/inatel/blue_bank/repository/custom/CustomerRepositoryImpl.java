package com.inatel.blue_bank.repository.custom;

import com.inatel.blue_bank.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Customer> findAllWithoutAccount(Specification<Customer> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);

        query.select(cb.construct(
                Customer.class,
                root.get("id"),
                root.get("fullName"),
                root.get("dob"),
                root.get("nationality"),
                root.get("phone"),
                root.get("email"),
                root.get("occupation"),
                root.get("docType"),
                root.get("docNumber"),
                root.get("createdAt"),
                root.get("updatedAt")
        ));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            query.where(predicate);
        }

        if (pageable.getSort().isSorted()) {
            query.orderBy(pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? cb.asc(root.get(order.getProperty()))
                            : cb.desc(root.get(order.getProperty())))
                    .toList());
        }

        List<Customer> resultList = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Customer> countRoot = countQuery.from(Customer.class);
        countQuery.select(cb.count(countRoot));
        if (spec != null) {
            Predicate predicate = spec.toPredicate(countRoot, countQuery, cb);
            countQuery.where(predicate);
        }
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }


}
