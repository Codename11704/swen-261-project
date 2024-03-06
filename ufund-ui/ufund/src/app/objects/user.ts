import { Need } from './need';

export interface User {
    user: string;
    pass: string;
    checkout: Map<number, number>;
}