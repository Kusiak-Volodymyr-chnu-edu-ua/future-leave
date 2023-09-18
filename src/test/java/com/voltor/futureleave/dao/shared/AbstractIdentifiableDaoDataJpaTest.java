package com.voltor.futureleave.dao.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.jpa.BaseCRUDRepository;
import com.voltor.futureleave.jpa.SessionRepository;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.service.AuthenticatedUserService;

/**
 * Use this as the base class for Data DAO JPA Tests when testing commonly used implementations
 */
@DataJpaTest
@ActiveProfiles(profiles = {"develop", "h2"})
public abstract class AbstractIdentifiableDaoDataJpaTest<T extends Identifiable> {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SessionRepository sessionRepository;

	/** Mockito Bean Mocks */
	@MockBean
	protected AuthenticatedUserService userAuthorizationService;

	@Mock
	private PasswordEncoder passwordEncoder;
  
	/**
	 * Returns the JPA EntityManager
	 * @return {@link jakarta.persistence.EntityManager}
	 */
	protected EntityManager getEntityManager(){
		return this.entityManager;
	}

	/**
	 * Returns the SessionRepository
	 * @return {@link SessionRepository}
	 */
	protected SessionRepository getSessionRepository() {
		return sessionRepository;
	}

	/**
	 * A mock of the UserAuthorizationService
	 * @return {@link AuthenticatedUserService}
	 */
	protected AuthenticatedUserService getUserAuthorizationService() {
		return this.userAuthorizationService;
	}

	/**
	 * The actual Spring Managed Entity Repository Bean
	 * @return {@link BaseCRUDRepository}
	 */
	protected abstract BaseCRUDRepository<T> getRepository();

	/**  Start {@link EntityType} Common tests */

	/**
	 * Create a new entity to validate.
	 * Important! Make sure there is at least one property that can break Bean Validation.
	 * @return
	 */
	protected abstract T createInvalidEntity();

	/**
	 * Creates a new Valid entity to persist before breaking constraint violation step
	 * @return entity
	 */
	protected abstract T createValidEntity();

	/**
	 * Returns an invalid entity that should break one or more constraint violations
	 * @return entity
	 */
	protected abstract T updateInvalidEntity(T createdEntity);

	@Test
	public void onCreateShouldThrowConstraintViolationException() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );
		T createInvalidEntity = createInvalidEntity();
		assertThrows( ConstraintViolationException.class, () -> getRepository().save(createInvalidEntity) );
	}

	@Test
	public void onUpdateShouldThrowConstraintViolationException() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );

		T entity = createValidEntity();
		getRepository().save(entity);

		T persistedEntity = getRepository().findById(entity.getId()).get();

		T updateInvalidEntity = updateInvalidEntity(persistedEntity);
		assertThrows( ConstraintViolationException.class, () -> {
			getRepository().save(updateInvalidEntity);
			entityManager.flush();
		} );
	}

}
