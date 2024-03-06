import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import { NeedService } from 'src/app/needService/need.service';
import { Need } from 'src/app/objects/need';

@Component({
  selector: 'app-need-manager',
  templateUrl: './need-manager.component.html',
  styleUrls: ['./need-manager.component.css']
})
export class NeedManagerComponent {
  needs: Need[] = [];

  form!: FormGroup

  needCreator!: FormGroup;
  search!: FormGroup
  responseCreate: String = "";
  noNeeds: String = ""

  constructor(private needService: NeedService, private authService: AuthService, private router: Router, private fb: FormBuilder) {
    
  }

  ngOnInit(): void {
  this.form = this.fb.group({
    'needsForm': ''
  })
  this.needCreator = new FormGroup({
    name: new FormControl(''),
    moneyNeeded: new FormControl(''),
    description: new FormControl(''),
  });
  this.search = new FormGroup({
    searchValue: new FormControl('')
  })
  this.searchNeeds('');
  }

  get getNeedsFormArray(): FormArray {
    return this.form.get('needsForm')?.value as FormArray;
  }

  getEditorResponse(i: number): String {
    return this.getNeedsFormArray.get(i.toString())?.get('response')?.value
  }

  getNeeds(): void {
    this.needService.getNeeds().subscribe((needs) => {
      this.needs = needs
      for(let i=0; i<needs.length; i++) {
        this.editorFormArray.push(this.fb.group({
          'response': ''
        }))
      }
      if(this.needs.length == 0) {
        this.noNeeds = "There are no Needs"
      }
    });
  }

  onSubmitChange(need: Need, i: number): void {
    this.editorFormArray.get(i.toString())?.get('response')?.setValue("")

    if(this.getNeedsFormArray.get(i.toString())?.value.get('name')?.value != null && !(this.getNeedsFormArray.get(i.toString())?.value.get('name')?.value.trim() === "")) {
      let good = true;
    
      for(let j=0; j<this.needs.length; j++) { 
        if(this.needs[j].name === this.getNeedsFormArray.get(i.toString())?.value.get('name')?.value.trim()) {
          this.getNeedsFormArray.get(i.toString())?.value.get('response')?.setValue("There is already a need with that name")
          good = false;
          break;
        }
      }
      if(good) 
        need.name = this.getNeedsFormArray.get(i.toString())?.value.get('name')?.value.trim();
    } 

    if(this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value != null && !(this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value.trim() === "")) {
      if(!isNaN(this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value.trim()))
        if(parseFloat(this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value) > 0) {
          need.moneyNeeded = parseFloat(this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value);
        } else {
          this.getNeedsFormArray.get(i.toString())?.value.get('response')?.setValue("Please enter a valid number")
        }
      else 
      this.getNeedsFormArray.get(i.toString())?.value.get('response')?.setValue("Please enter a valid number")
    }

    if(this.getNeedsFormArray.get(i.toString())?.value.get('description')?.value != null && !(this.getNeedsFormArray.get(i.toString())?.value.get('description')?.value.trim() === "")) {
      need.description = this.getNeedsFormArray.get(i.toString())?.value.get('description')?.value.trim()
    }
    
    console.log(need);
    this.needService.updateNeed(need).subscribe(
      (need) => {
        this.router.navigate(['./admin']);
      });
    this.getNeedsFormArray.get(i.toString())?.value.get('name')?.value;
    this.getNeedsFormArray.get(i.toString())?.value.get('moneyNeeded')?.value;
    this.getNeedsFormArray.get(i.toString())?.value.get('description')?.value;
  }

  onSubmitNew(): void {
    let good = true;

    this.responseCreate = ""
    this.editorFormArray.get("2")?.get('response')?.setValue("")

    if(this.needCreator.get('name')?.value.trim() === "") {
      this.responseCreate = "Please enter a name"
      good = false;
    }
    if(good) {
      for(let i=0; i<this.needs.length; i++) {
        if(this.needs[i].name === this.needCreator.get('name')?.value.trim()) {
          this.responseCreate = "There is already a need with that name"
          good = false;
          break
        }
      }
    } 

    if(this.needCreator.get('moneyNeeded')?.value === "" && good) {
      this.responseCreate = "Please enter a number"
      good = false;
    }

    if(good) {
      if(isNaN(this.needCreator.get('moneyNeeded')?.value)) {
        this.responseCreate = "Please enter a valid number"
        good = false;
      } else if(parseFloat(this.needCreator.get('moneyNeeded')?.value) <= 0) {
        this.responseCreate = "Please enter a valid number"
        good = false;
      }
    }
    
    if(this.needCreator.get('description')?.value.trim() === "" && good) {
      this.responseCreate = "Please enter a description"
      good = false;
    } 

    if(good){
      this.needService.createNeed({id: 0, name: this.needCreator.get('name')?.value.trim(), moneyEarned: 0.00, moneyNeeded: parseFloat(this.needCreator.get('moneyNeeded')?.value), description: this.needCreator.get('description')?.value.trim(), count: 0}).subscribe(
        (need) => {
          this.router.navigate(['./admin']);
          this.getNeeds()
          this.needCreator.get('name')?.setValue("")
          this.needCreator.get('moneyNeeded')?.setValue("")
          this.needCreator.get('description')?.setValue("")
        });
    }
  }

  searchNeeds(search: string): void {
    this.needService.searchNeeds(search.trim()).subscribe((needs) => {
      this.needs = needs;
      for(let  i=0; i<needs.length; i++) {
        this.getNeedsFormArray.push({
          'name': '',
          'moneyNeeded': '',
          'description':'',
          'response': ''
        });
      }
      if(this.needs.length == 0) {
        this.noNeeds = "There are currently no needs with " + search.trim()
      } else {
        this.noNeeds = ""
      }
    });
  }

  onSubmitDelete(need: Need) {
    this.needService.deleteNeed(need.id.toString()).subscribe((need) => {
      this.getNeeds()
    });
    
  }

  logout() {
    this.authService.logout();
  }
}
