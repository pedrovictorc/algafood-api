package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

public interface VendasReportService {
	
	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet);
}
