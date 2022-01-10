package com.crud.main.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crud.main.model.Convidado;
import com.crud.main.model.Evento;
import com.crud.main.repository.ConvidadoRepository;
import com.crud.main.repository.EventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private ConvidadoRepository convidadoRepository;

	@GetMapping("/cadastrarEvento")
	public String form() {
		return "formEvento";
	}
	
	@GetMapping("/atualizarEvento/{codigo}")
	public ModelAndView attEvento(@PathVariable("codigo") long codigo) {
		ModelAndView mv = new ModelAndView("formEventoAtt");
		mv.addObject("evento", eventoRepository.findByCodigo(codigo)); 
		return mv; 
	}
	
	@PostMapping("/atualizar/{codigo}")
	public String atualizaEvento(@PathVariable("codigo") long codigo,  @Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			evento.setCodigo(codigo);
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			String codigoS = String.valueOf(codigo);
			return "redirect:/atualizarEvento/" + codigoS;
		}
		Evento umEvento = eventoRepository.findByCodigo(codigo);
		umEvento.setNome(evento.getNome());
		umEvento.setLocal(evento.getLocal());
		umEvento.setHorario(evento.getHorario());
		umEvento.setData(evento.getData());
			
		eventoRepository.save(umEvento);
		return "redirect:/eventos";
	}
	
	@PostMapping("/cadastrarEvento")
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}
		eventoRepository.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:/cadastrarEvento";
	}
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("eventos", eventoRepository.findAll());
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = eventoRepository.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("detalhesEvento");
		mv.addObject("evento", evento);
		
		Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;
	}
	
	@RequestMapping("/deletarEvento/{codigo}")
	public String deletarEvento(@PathVariable("codigo") long codigo) {
		Evento evento = eventoRepository.findByCodigo(codigo);
		eventoRepository.delete(evento);
		
		return "redirect:/eventos";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = convidadoRepository.findByRg(rg);
		convidadoRepository.delete(convidado);
		
		Evento evento = convidado.getEvento();
		long codigoLong = evento.getCodigo();
		String codigo = String.valueOf(codigoLong);
		return "redirect:/" + codigo;
	}
	
	@PostMapping("/{codigo}")
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}";
		}
		Evento evento = eventoRepository.findByCodigo(codigo);
		convidado.setEvento(evento);
		convidadoRepository.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
}
