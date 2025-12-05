package com.inatel.blue_bank.service;

import com.inatel.blue_bank.exception.DeniedOperationException;
import com.inatel.blue_bank.exception.EntityNotFoundException;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Investment;
import com.inatel.blue_bank.repository.InvestmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvestmentServiceTest {

    @Mock
    private InvestmentRepository repository;

    @InjectMocks
    private InvestmentService service;

    @Test
    public void saveShouldNotThrowExceptionTest(){
        Investment investment = new Investment();
        investment.setTicker("AAPL");

        when(repository.save(investment)).thenReturn(investment);
        Investment saved = service.save(investment);
        assertEquals(investment.getTicker(), saved.getTicker());
        verify(repository, times(1)).save(investment);
    }

    @Test
    public void saveShouldThrowExceptionTest(){
        Investment investment = new Investment();

        DeniedOperationException ex = assertThrows(
                DeniedOperationException.class,
                () -> service.save(investment)
        );

        assertEquals("Ticker is null", ex.getMessage());
    }

    @Test
    public void findByIdTest(){
        Investment investment = new Investment();
        UUID id = UUID.randomUUID();
        investment.setId(id);

        when(repository.findById(investment.getId())).thenReturn(Optional.of(investment));
        Optional<Investment> optionalInvestment = service.findById(id);
        assertEquals(investment, optionalInvestment.get());
        verify(repository, times(1)).findById(investment.getId());
    }

    @Test
    public void searchTest(){
        List<Investment> investments = new ArrayList<>();
        Investment investment1 = new Investment();
        Investment investment2 = new Investment();

        investment1.setTicker("AAPL");
        investment2.setTicker("WDOL");
        investment1.setShare(2.0);
        investment2.setShare(3.0);
        investments.add(investment1);
        investments.add(investment2);

        Account account = new Account();
        account.setInvestments(investments);
        when(repository.findByAccount(account)).thenReturn(investments);
        List<Investment> investmentsList = service.searchByAccount(account);
        assertEquals(investments, investmentsList);
        verify(repository, times(1)).findByAccount(account);
    }

    @Test
    public void deleteTest(){
        Investment investment = new Investment();
        doNothing().when(repository).delete(investment);
        service.delete(investment);
        verify(repository, times(1)).delete(investment);
    }

    @Test
    public void updateShouldThrowExceptionWhenInvestmentNotFoundTest(){
        Investment investment = new Investment();
        when(repository.findById(investment.getId())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.update(investment)
        );

        assertEquals("Investment not found", ex.getMessage());
    }

    @Test
    public void updateShouldThrowExceptionWhenTickersDoNotMatchTest(){
        Investment investment = new Investment();
        investment.setTicker("AAPL");
        Investment investment2 = new Investment();
        investment2.setTicker("WDOL");
        when(repository.findById(investment.getId())).thenReturn(Optional.of(investment2));

        DeniedOperationException ex = assertThrows(
                DeniedOperationException.class,
                () -> service.update(investment)
        );

        assertEquals("Ticker does not match", ex.getMessage());

        verify(repository, times(1)).findById(investment.getId());
    }

    @Test
    public void updateShouldNotThrowExceptionTest(){
        Investment investment1 = new Investment(); // o que foi passado como parametro na chamada do service.update
        Investment investment2 = new Investment(); // o que ele achou no banco de dados
        investment1.setTicker("AAPL");
        investment2.setTicker("AAPL");

        when(repository.findById(investment1.getId())).thenReturn(Optional.of(investment2));

        assertDoesNotThrow(() -> service.update(investment1));

        verify(repository, times(1)).findById(investment1.getId());
    }
}
