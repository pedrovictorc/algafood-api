package com.algaworks.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendasReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendasReportService{


	@Autowired
	private VendaQueryService service;
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		try {
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

			var vendasDiarias = service.consultarVendaDiarias(filtro, timeOffSet);
			var datasource = new JRBeanCollectionDataSource(vendasDiarias);


			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, datasource);

			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possivel emitir relatório de vendas diárias",e);
		}


	}

}
