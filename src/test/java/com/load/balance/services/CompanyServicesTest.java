package com.load.balance.services;

import com.load.balance.application.dtos.company.CreateCompanyDTO;
import com.load.balance.application.dtos.company.JoinCompanyDTO;
import com.load.balance.application.exceptions.users.UserNotFoundException;
import com.load.balance.application.shared.SlugGenerator;
import com.load.balance.application.usecase.companies.AddMemberAtCompany;
import com.load.balance.application.usecase.companies.CheckUserInCompanyUseCase;
import com.load.balance.application.usecase.companies.FindCompanyOrThrowUseCase;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.enums.StatusCompany;
import com.load.balance.models.Company;
import com.load.balance.models.Users;
import com.load.balance.repositories.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServicesTest {

    @Mock private CompanyRepository companyRepository;
    @Mock private SlugGenerator slugGenerator;
    @Mock private FindUserOrThrowUseCase findUserOrThrowUseCase;
    @Mock private AddMemberAtCompany addMemberAtCompany;
    @Mock private FindCompanyOrThrowUseCase findCompanyOrThrowUseCase;
    @Mock private CheckUserInCompanyUseCase checkUserInCompanyUseCase;
    @Mock private HttpSession session;

    @InjectMocks
    private CompanyServices companyServices;

    private UUID userId;
    private Users mockUser;
    private Users mockSessionUser;
    private Company mockCompany;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        mockUser = Users.builder()
                .username("testuser")
                .email("test@test.com")
                .password("encoded")
                .build();

        mockSessionUser = Users.builder()
                .username("sessionuser")
                .email("session@test.com")
                .password("encoded")
                .build();

        mockCompany = Company.builder()
                .name("Test Company")
                .description("A test company")
                .slug("test-company-abc123")
                .status(StatusCompany.ACTIVE)
                .createdBy(mockUser)
                .build();
    }

    @Test
    @DisplayName("createCompany: You must create a company and add the creator as a member")
    void createCompany_success() {
        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(findUserOrThrowUseCase.byId(userId)).thenReturn(mockUser);
        when(slugGenerator.generate("Test Company")).thenReturn("test-company-abc123");
        when(companyRepository.save(any(Company.class))).thenReturn(mockCompany);

        Company result = companyServices.createCompany(
                new CreateCompanyDTO("Test Company", "A test company"), session
        );

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Company");
        assertThat(result.getStatus()).isEqualTo(StatusCompany.ACTIVE);

        verify(companyRepository).save(any(Company.class));
        verify(addMemberAtCompany).execute(eq(mockCompany), eq(mockUser));
    }

    @Test
    @DisplayName("createCompany: An exception should be thrown if userId is not in the session")
    void createCompany_missingSession_throwsException() {
        when(session.getAttribute("userId")).thenReturn(null);

        assertThatThrownBy(() ->
                companyServices.createCompany(new CreateCompanyDTO("Test", null), session)
        ).isInstanceOf(NullPointerException.class);

        verifyNoInteractions(companyRepository);
    }

    @Test
    @DisplayName("createCompany: An exception should be thrown if the user does not exist")
    void createCompany_userNotFound_throwsException() {
        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(findUserOrThrowUseCase.byId(userId))
                .thenThrow(new UserNotFoundException());

        assertThatThrownBy(() ->
                companyServices.createCompany(new CreateCompanyDTO("Test", null), session)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verifyNoInteractions(companyRepository);
    }

    @Test
    @DisplayName("joinCompany: You must add the member successfully")
    void joinCompany_success() {
        UUID targetUserId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();

        Users targetUser = Users.builder()
                .username("newmember")
                .email("new@test.com")
                .password("encoded")
                .build();

        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(findUserOrThrowUseCase.byId(userId)).thenReturn(mockUser);
        when(findUserOrThrowUseCase.byId(targetUserId)).thenReturn(targetUser);
        when(findCompanyOrThrowUseCase.byId(companyId)).thenReturn(mockCompany);
        doNothing().when(checkUserInCompanyUseCase).exist(userId, companyId);
        doNothing().when(checkUserInCompanyUseCase).notExist(targetUserId, companyId);

        companyServices.joinCompany(new JoinCompanyDTO(targetUserId, companyId), session);

        verify(addMemberAtCompany).execute(eq(mockCompany), eq(targetUser));
    }

    @Test
    @DisplayName("joinCompany: An exception should be made if the person adding the item is not a member of the company")
    void joinCompany_sessionUserNotMember_throwsException() {
        UUID targetUserId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();

        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(findUserOrThrowUseCase.byId(userId)).thenReturn(mockUser);
        when(findUserOrThrowUseCase.byId(targetUserId)).thenReturn(mockSessionUser);
        when(findCompanyOrThrowUseCase.byId(companyId)).thenReturn(mockCompany);
        doThrow(new RuntimeException("User is not a member of the company"))
                .when(checkUserInCompanyUseCase).exist(userId, companyId);

        assertThatThrownBy(() ->
                companyServices.joinCompany(new JoinCompanyDTO(targetUserId, companyId), session)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("User is not a member of the company");

        verify(addMemberAtCompany, never()).execute(any(), any());
    }

    @Test
    @DisplayName("joinCompany: An exception should be thrown if the target user is already a member")
    void joinCompany_targetAlreadyMember_throwsException() {
        UUID targetUserId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();

        Users targetUser = Users.builder()
                .username("existing")
                .email("existing@test.com")
                .password("encoded")
                .build();

        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(findUserOrThrowUseCase.byId(userId)).thenReturn(mockUser);
        when(findUserOrThrowUseCase.byId(targetUserId)).thenReturn(targetUser);
        when(findCompanyOrThrowUseCase.byId(companyId)).thenReturn(mockCompany);
        doNothing().when(checkUserInCompanyUseCase).exist(userId, companyId);
        doThrow(new RuntimeException("User is already a member of the company"))
                .when(checkUserInCompanyUseCase).notExist(targetUserId, companyId);

        assertThatThrownBy(() ->
                companyServices.joinCompany(new JoinCompanyDTO(targetUserId, companyId), session)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("User is already a member of the company");

        verify(addMemberAtCompany, never()).execute(any(), any());
    }

    @Test
    @DisplayName("findBySlug: should return a list of companies by slug")
    void findBySlug_success() {
        String slug = "test-company-abc123";
        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(companyRepository.companiesBySlug(eq(slug), any())).thenReturn(List.of(mockCompany));

        List<Company> result = companyServices.findBySlug(slug, session);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Test Company");
    }

    @Test
    @DisplayName("findBySlug: It should return an empty list if no company is found")
    void findBySlug_notFound_returnsEmpty() {
        when(session.getAttribute("userId")).thenReturn(userId.toString());
        when(companyRepository.companiesBySlug(any(), any())).thenReturn(List.of());

        List<Company> result = companyServices.findBySlug("nao-existe", session);

        assertThat(result).isEmpty();
    }
}

