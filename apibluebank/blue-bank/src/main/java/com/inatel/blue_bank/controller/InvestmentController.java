@RestController
@RequestMapping("investments")
@RequiredArgsConstructor
public class InvestmentController implements GernericController {
  private final InvestmentService service;
  
  @PostMapping
  public ResponseEntity<Void> save(@RequestBody Investment investment){
    service.save(investment);
    URI location = generateHeaderLocation(invetment.getId());
    return ResponseEntity.created(location).build();
  }

    @GetMapping("{id}")
    public ResponseEntity<Investment> getDetails(@PathVariable("id") String id) {
        UUID investmentId = UUID.fromString(id);

        return service
                .findById(invetmentId)
                .map(investment -> {
                    return ResponseEntity.ok(investment);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
/*
    @GetMapping
    public ResponseEntity<List<Investment>> search(
            @RequestParam(value = "account-number", required = false) String accountNumber,
            @RequestParam(value = "branch-code", required = false) String branchCode) {
        .findById()
        List<Investment> searchResult = service.searchByAccount(accountNumber, branchCode);
        List<Investment> list = searchResult
                .stream()
                .map(authorMapper::toDTO).toList();
        return ResponseEntity.ok(list);
    }
*/
    @PutMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") String id, @RequestBody Investment investment) {

        UUID investmentId = UUID.fromString(id);
        Optional<Investment> investmentOptional = service.findById(investmentId);

        if(investmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Investment investmentFound = investmentOptional.get();
        investmentFound.setName(investment.getName);
        investmentFound.setShare(investment.getShare);
        investmentFound.setCostPerShare(investment.getCostPerShare);

        service.update(investmentFound);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        UUID investmentId = UUID.fromString(id);
        Optional<Investment> investmentOptional = service.findById(investmentId);

        if(investmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(investmentOptional.get());

        return ResponseEntity.noContent().build();
    }
}

}
